package com.example.wire.core.network.websocket

sealed interface SocketEvent {

    data class MessageReceived(
        val message: String
    ) : SocketEvent

    data class Error(
        val message: String
    ) : SocketEvent

    object Connected : SocketEvent

    object Disconnected : SocketEvent
}