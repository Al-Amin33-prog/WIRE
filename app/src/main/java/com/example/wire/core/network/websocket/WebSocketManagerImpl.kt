package com.example.wire.core.network.websocket


import com.example.wire.core.di.ApplicationScope
import com.example.wire.core.domain.dispatcher.CoroutineDispatchers
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.websocket.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WebSocketManagerImpl @Inject constructor(
    @ApplicationScope private val applicationScope: CoroutineScope,
    private val dispatchers: CoroutineDispatchers
    // DAOs and Repositories REMOVED - Logic moved to Processors
) : WebSocketManager {

    private val _state = MutableStateFlow<WebSocketState>(WebSocketState.Disconnected)
    private val incomingMessages = MutableSharedFlow<String>()
    private val client = HttpClient(CIO) { install(WebSockets) }
    private var session: DefaultClientWebSocketSession? = null

    override fun connectionState() = _state.asStateFlow()
    override fun observeMessages(): Flow<String> = incomingMessages

    override suspend fun connect() {
        try {
            session = client.webSocketSession { url(WebSocketConfig.BASE_URL) }
            _state.value = WebSocketState.Connected
            listenForMessages()
        } catch (e: Exception) {
            _state.value = WebSocketState.Disconnected
        }
    }

    private fun listenForMessages() {
        applicationScope.launch(dispatchers.io) {
            try {
                session?.incoming?.consumeEach { frame ->
                    if (frame is Frame.Text) {
                        // THE SYMPHONY: Just emit the raw string.
                        // The Switchboard (UseCase) will catch this and send it to Processors.
                        incomingMessages.emit(frame.readText())
                    }
                }
            } catch (e: Exception) {
                _state.value = WebSocketState.Disconnected
            }
        }
    }

    override suspend fun sendMessage(message: String) {
        session?.send(Frame.Text(message))
    }

    override suspend fun disconnect() {
        session?.close()
        _state.value = WebSocketState.Disconnected
    }

    // Typing state logic can stay or move to a TypingProcessor
    private val _isTyping = MutableStateFlow(false)
    override fun isTyping() = _isTyping.asStateFlow()
}