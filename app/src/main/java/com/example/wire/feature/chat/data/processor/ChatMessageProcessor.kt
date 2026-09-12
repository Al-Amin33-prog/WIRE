package com.example.wire.feature.chat.data.processor

import com.example.wire.core.database.dao.ChatDao
import com.example.wire.core.database.dao.MessageDao
import com.example.wire.core.network.notification.NotificationHandler
import com.example.wire.core.network.websocket.WebSocketProcessor
import com.example.wire.feature.auth.domain.repository.AuthRepository
import com.example.wire.feature.chat.data.mapper.toEntity
import com.example.wire.feature.chat.data.remote.dto.ChatActionDto
import javax.inject.Inject

class ChatMessageProcessor @Inject constructor(
    private val messageDao: MessageDao,
    private val chatDao: ChatDao,
    private val notificationHandler: NotificationHandler,
    private val authRepository: AuthRepository
) : WebSocketProcessor {
    override val action = "SEND"

    override suspend fun process(actionDto: ChatActionDto) {
        val msg = actionDto.message ?: return
        val currentUserId = authRepository.getCurrentUser()?.uid ?: ""
        
        // Robust Chat ID resolution: the chat ID is always the ID of the OTHER party in the conversation
        val resolvedChatId = if (msg.senderId == currentUserId) msg.receiverId else msg.senderId

        messageDao.insertMessage(msg.toEntity(chatId = resolvedChatId))
        chatDao.updateChatPreview(
            chatId = resolvedChatId,
            lastMessage = msg.content,
            timestamp = System.currentTimeMillis()
        )

        // Only show notification for incoming messages from others
        if (msg.senderId != currentUserId) {
            notificationHandler.showChatNotification(
                chatId = resolvedChatId,
                senderName = msg.metadata?.get("senderName") ?: "New Message",
                message = msg.content
            )
        }
    }
}
