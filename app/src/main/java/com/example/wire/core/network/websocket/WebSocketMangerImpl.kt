package com.example.wire.core.network.websocket

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
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WebSocketManagerImpl @Inject constructor() : WebSocketManager {

    private val _state = MutableStateFlow<WebSocketState>(WebSocketState.Disconnected)

    private val client = HttpClient(CIO) {
        install(WebSockets)
    }

    private var session: DefaultClientWebSocketSession? = null

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
                        incomingMessages.emit(frame.readText())
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
