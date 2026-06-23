package com.example.wire.core.network.websocket

sealed interface WebSocketState {

    object Connected : WebSocketState

    object Connecting : WebSocketState

    object Disconnected : WebSocketState

    data class Error(
        val message: String
    ) : WebSocketState
}