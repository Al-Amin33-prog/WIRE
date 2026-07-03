package com.example.wire.feature.chat.data.processor



import com.example.wire.core.database.dao.ChatDao
import com.example.wire.core.database.dao.MessageDao
import com.example.wire.core.network.notification.NotificationHandler
import com.example.wire.core.network.websocket.WebSocketProcessor
import com.example.wire.feature.chat.data.mapper.toEntity
import com.example.wire.feature.chat.data.remote.dto.ChatActionDto
import javax.inject.Inject

class ChatMessageProcessor @Inject constructor(
    private val messageDao: MessageDao,
    private val chatDao: ChatDao,
    private val notificationHandler: NotificationHandler
) : WebSocketProcessor {
    override val action = "SEND"

    override suspend fun process(actionDto: ChatActionDto) {
        val msg = actionDto.message ?: return


        messageDao.insertMessage(msg.toEntity(chatId = msg.senderId))
        chatDao.updateChatPreview(
            chatId = msg.senderId,
            lastMessage = msg.content,
            timestamp = System.currentTimeMillis()
        )

        // UI Alert
        notificationHandler.showChatNotification(
            chatId = msg.senderId,
            senderName = msg.metadata?.get("senderName") ?: "New Message",
            message = msg.content
        )
    }
}