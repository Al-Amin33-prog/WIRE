package com.example.wire.feature.chat.data.repository

import com.example.wire.core.network.websocket.WebSocketManager
import com.example.wire.feature.chat.domain.model.Message
import com.example.wire.feature.chat.domain.model.MessageType
import com.example.wire.feature.chat.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
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

    override fun observeMessages(
        chatId: String
    ): Flow<List<Message>> {

        return webSocketManager
            .observeMessages()
            .map { text ->

                listOf(
                    Message(
                        id = "",
                        senderId = "",
                        type = MessageType.TEXT,
                        content = text,
                        timestamp = System.currentTimeMillis()
                    )
                )
            }
    }

    override suspend fun loadChatHistory(
        chatId: String
    ): List<Message> {

        return emptyList()
    }
}