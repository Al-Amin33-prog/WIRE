package com.example.wire.core.network.websocket

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface WebSocketManager {

    suspend fun connect()

    suspend fun disconnect()

    suspend fun sendMessage(
        message: String
    )

    fun observeMessages(): Flow<String>

    fun connectionState(): StateFlow<WebSocketState>
    fun isTyping(): StateFlow<Boolean>
}