package com.example.wire.feature.chat.domain.repository

import com.example.wire.feature.chat.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface ChatRepository {

    suspend fun connect()

    suspend fun disconnect()

    fun observeMessages(
        chatId: String
    ): Flow<List<Message>>

    suspend fun sendMessage(
        chatId: String,
        content: String
    )

    suspend fun loadChatHistory(
        chatId: String
    ): List<Message>
}