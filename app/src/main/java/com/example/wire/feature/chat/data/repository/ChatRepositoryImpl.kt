package com.example.wire.feature.chat.data.repository

import com.example.wire.core.network.websocket.WebSocketManager
import com.example.wire.feature.chat.data.remote.dto.ChatActionDto
import com.example.wire.feature.chat.data.remote.dto.ChatApiService
import com.example.wire.feature.chat.domain.model.Message
import com.example.wire.feature.chat.domain.model.MessageType
import com.example.wire.feature.chat.data.mapper.toDomain
import com.example.wire.feature.chat.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val api: ChatApiService,
    private val webSocketManager: WebSocketManager
) : ChatRepository {

    override suspend fun connect() {
        webSocketManager.connect()
    }

    override suspend fun disconnect() {
        webSocketManager.disconnect()
    }

    override suspend fun sendMessage(
        chatId: String,
        content: String
    ) {
        webSocketManager.sendMessage(content)
    }

    override fun observeMessages(chatId: String): Flow<List<Message>> {
        return webSocketManager        .observeMessages()
            .map { json ->
                val dto = Json.decodeFromString<ChatActionDto>(json)

                // Only map and return if there is a message
                dto.message?.let {
                    listOf(it) // If it's already domain type, no .toDomain() needed
                } ?: emptyList()
            }
    }

    override suspend fun loadChatHistory(
        chatId: String
    ): List<Message> {

        return api.getChatHistory(chatId)
    }
}