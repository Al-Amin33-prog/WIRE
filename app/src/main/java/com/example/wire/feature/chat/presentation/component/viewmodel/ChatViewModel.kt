package com.example.wire.feature.chat.presentation.component.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wire.core.common.util.AppError
import com.example.wire.core.common.util.Resource
import com.example.wire.core.data.repository.SyncRepository
import com.example.wire.feature.auth.domain.repository.AuthRepository
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
    private val chatUseCases: ChatUseCases,
    private val authRepository: AuthRepository,
    private val syncRepository: SyncRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState = _uiState.asStateFlow()

    private val currentChatId = "default_chat_id"

    init {
        onEvent(ChatUiEvent.LoadHistory(currentChatId))
        connectAndObserve()
        loadUserProfile()
    }

    fun onEvent(event: ChatUiEvent) {
        when (event) {
            is ChatUiEvent.MessageChanged -> {
                _uiState.update { it.copy(messageText = event.message) }
            }
            is ChatUiEvent.SendMessage -> sendMessage()
            is ChatUiEvent.Connect -> connectAndObserve()
            is ChatUiEvent.LoadHistory -> loadHistory(event.chatId)
            is ChatUiEvent.Disconnect -> {
                viewModelScope.launch {
                    chatUseCases.disconnectFromChat()
                    _uiState.update { it.copy(isConnected = false) }
                }
            }
            is ChatUiEvent.Refresh -> {
                viewModelScope.launch {
                    syncRepository.syncAll()
                }
            }
            is ChatUiEvent.MessageLongClick -> {
                _uiState.update {
                    it.copy(
                        selectedMessageId = event.messageId,
                        showMessageActions = true
                    )
                }
            }
            is ChatUiEvent.DismissMessageActions -> {
                _uiState.update {
                    it.copy(
                        selectedMessageId = null,
                        showMessageActions = false
                    )
                }
            }
            is ChatUiEvent.DeleteMessage -> {
                deleteMessage(event.messageId)

            }
        }
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            val user = authRepository.getCurrentUser()
            val name = user?.displayName ?: "User"
            _uiState.update { it.copy(displayName = name) }
        }
    }

    private fun connectAndObserve() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            // 1. Handle Connection Resource
            when (val connectionResult = chatUseCases.connectToChat()) {
                is Resource.Success -> {
                    _uiState.update { it.copy(isConnected = true, isLoading = false, error = null) }
                }
                is Resource.Error -> {
                    _uiState.update { it.copy(
                        isConnected = false,
                        isLoading = false,
                        error = mapError(connectionResult.error)
                    )}
                }
                is Resource.Loading -> { }
            }

            // 2. Observe Messages (This remains a Flow)
            chatUseCases.observeMessages(currentChatId)
                .onEach { newMessageList ->
                    _uiState.update { state ->
                        state.copy(messages = newMessageList)
                    }
                }
                .catch { e ->
                    _uiState.update { it.copy(error = e.message, isConnected = false) }
                }
                .launchIn(this)
        }
    }

    private fun loadHistory(chatId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            // FIX: Handle the Resource result
            when (val result = chatUseCases.loadChatHistory(chatId)) {
                is Resource.Success -> {
                    _uiState.update { it.copy(
                        messages = result.data, // Access .data to get the List<Message>
                        isLoading = false
                    )}
                }
                is Resource.Error -> {
                    _uiState.update { it.copy(
                        error = mapError(result.error),
                        isLoading = false
                    )}
                }
                is Resource.Loading -> { }
            }
        }
    }

    private fun sendMessage() {
        val text = _uiState.value.messageText
        if (text.isBlank()) return

        viewModelScope.launch {
            // FIX: Handle the Resource result
            when (val result = chatUseCases.sendMessage(currentChatId, text)) {
                is Resource.Success -> {
                    _uiState.update { it.copy(messageText = "", error = null) }
                }
                is Resource.Error -> {
                    _uiState.update { it.copy(error = mapError(result.error)) }
                }
                is Resource.Loading -> { }
            }
        }
    }

    // Helper to map AppError to user-friendly strings (Matches your AuthViewModel logic)
    private fun mapError(error: AppError): String {
        return when (error) {
            is AppError.Validation -> error.message
            is AppError.Network.NoInternet -> "No internet connection."
            is AppError.Network.Timeout -> "Connection timed out."
            is AppError.Network.Unknown -> error.message ?: "An unexpected error occurred"
            else -> "An error occurred"
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.launch(NonCancellable) {
            chatUseCases.disconnectFromChat()
        }
    }
    private fun deleteMessage(messageId: String) {
        viewModelScope.launch {
            //1. Optimistic UI: Close the menu immediately
            _uiState.update {
                it.copy(
                    showMessageActions = false,
                    selectedMessageId = null
                )
            }

            // 2. Execute deletion logic via UseCase
            // We assume you will add 'deleteMessage' to your ChatUseCases bundle
            when (val result = chatUseCases.deleteMessage(currentChatId, messageId)) {
                is Resource.Success -> {
                    // Database is updated, Flow observation handles the UI update
                }
                is Resource.Error -> {
                    _uiState.update { it.copy(error = mapError(result.error)) }
                }
                is Resource.Loading -> { }
            }
        }
    }
}