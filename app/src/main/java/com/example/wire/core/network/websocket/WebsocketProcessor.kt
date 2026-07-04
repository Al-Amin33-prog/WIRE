package com.example.wire.core.network.websocket

import com.example.wire.feature.chat.data.remote.dto.ChatActionDto

interface WebSocketProcessor {

    val action: String
    suspend fun process(actionDto: ChatActionDto)
}