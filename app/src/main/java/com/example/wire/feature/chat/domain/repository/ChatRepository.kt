package com.example.wire.feature.chat.domain.repository

import androidx.compose.ui.input.key.Key
import com.example.wire.core.common.util.Resource
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
    ): Resource<Unit>

    suspend fun loadChatHistory(
        chatId: String
    ): Resource<List<Message>>

    suspend fun deleteMessage(
        chatId:
        String, messageId: String
    )
    : Resource<Unit>
}