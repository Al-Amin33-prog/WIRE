package com.example.wire.feature.chat.presentation.component.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wire.feature.chat.data.wrapper.ChatUseCases
import com.example.wire.feature.chat.presentation.component.event.ChatUiEvent
import com.example.wire.feature.chat.presentation.component.state.ChatUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatUseCases: ChatUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState = _uiState.asStateFlow()

    // Consider getting this from a SavedStateHandle for real navigation
    private val currentChatId = "default_chat_id"

    init {
        onEvent(ChatUiEvent.LoadHistory(currentChatId))
        connectAndObserve()
    }

    fun onEvent(event: ChatUiEvent) {
        when (event) {
            is ChatUiEvent.MessageChanged -> {
                _uiState.update { it.copy(messageText = event.message) }
            }
            is ChatUiEvent.SendMessage -> {
                sendMessage()
            }
            is ChatUiEvent.Connect -> {
                connectAndObserve()
            }
            is ChatUiEvent.LoadHistory -> {
                loadHistory(event.chatId)
            }
            is ChatUiEvent.Disconnect -> {
                viewModelScope.launch {
                    chatUseCases.disconnectFromChat()
                }
            }
        }
    }

    private fun connectAndObserve() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            // Connect to WebSocket
            chatUseCases.connectToChat()

            // Observe incoming messages
            chatUseCases.observeMessages(currentChatId)
                .onEach { newMessageList ->
                    // Note: ensure your UseCase/Repo returns List<Message>
                    // or adjust logic if it returns a single Message
                    _uiState.update { state ->
                        state.copy(
                            messages = state.messages + newMessageList,
                            isConnected = true,
                            isLoading = false
                        )
                    }
                }
                .catch { e ->
                    _uiState.update { it.copy(error = e.message, isConnected = false, isLoading = false) }
                }
                .launchIn(this)
        }
    }

    private fun loadHistory(chatId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                // 1. Actually get the data
                val history = chatUseCases.loadChatHistory(chatId)

                // 2. Update the state (Assuming history is a List<Message>)
                _uiState.update { it.copy(
                    messages = history,
                    isLoading = false
                )}
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    error = "Could not load history",
                    isLoading = false
                )}
            }
        }
    }

    private fun sendMessage() {
        val text = _uiState.value.messageText
        if (text.isBlank()) return

        viewModelScope.launch {
            try {
                chatUseCases.sendMessage(currentChatId, text)
                _uiState.update { it.copy(messageText = "") }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = "Failed to send: ${e.message}") }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        // Use NonCancellable to ensure the disconnect signal is sent
        // even though the ViewModelScope is being cancelled
        viewModelScope.launch(NonCancellable) {
            chatUseCases.disconnectFromChat()
        }
    }
}