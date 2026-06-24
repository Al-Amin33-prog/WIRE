package com.example.wire.core.network.websocket



import com.example.wire.feature.chat.data.remote.dto.ChatActionDto
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.websocket.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WebSocketManagerImpl @Inject constructor() : WebSocketManager {

    private val _state = MutableStateFlow<WebSocketState>(WebSocketState.Disconnected)

    private val client = HttpClient(CIO) {
        install(WebSockets)
    }

    private var session: DefaultClientWebSocketSession? = null
    private val _isTyping = MutableStateFlow(false)
     override fun isTyping() = _isTyping.asStateFlow()

    private val incomingMessages = MutableSharedFlow<String>()

    override fun connectionState() = _state.asStateFlow()

    override suspend fun connect() {
        try {
            session = client.webSocketSession {
                url(WebSocketConfig.BASE_URL)
            }
            _state.value = WebSocketState.Connected
            listenForMessages()
        } catch (e: Exception) {
            _state.value = WebSocketState.Disconnected
        }
    }





    private fun listenForMessages() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                session?.incoming?.consumeEach { frame ->
                    if (frame is Frame.Text) {
                        val jsonString = frame.readText()

                        try {
                            val chatAction = Json.decodeFromString<ChatActionDto>(jsonString)

                            when(chatAction.action) {
                                "TYPING_ON" -> _isTyping.value = true
                                "TYPING_OFF" -> _isTyping.value = false
                                "DELETE" -> {
                                    // Logic to notify repository to remove messageId
                                    incomingMessages.emit(jsonString)
                                }
                                else -> {
                                    // For SEND, EDIT, or PAYMENT, we emit the whole JSON
                                    // so the Repository can parse the Message object
                                    incomingMessages.emit(jsonString)
                                }
                            }
                        } catch (e: Exception) {
                            // Fallback: If it's just a raw string (for testing)
                            incomingMessages.emit(jsonString)
                        }
                    }
                }
            } catch (e: Exception) {
                _state.value = WebSocketState.Disconnected
            }
        }
    }

    override suspend fun disconnect() {
        session?.close()
        _state.value = WebSocketState.Disconnected
    }

    override suspend fun sendMessage(message: String) {
        session?.send(Frame.Text(message))
    }

    override fun observeMessages(): Flow<String> {
        return incomingMessages
    }
}
