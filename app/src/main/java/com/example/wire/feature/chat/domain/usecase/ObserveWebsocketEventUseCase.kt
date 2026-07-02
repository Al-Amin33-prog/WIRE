package com.example.wire.feature.chat.domain.usecase

import com.example.wire.core.common.util.AvatarUtils
import com.example.wire.core.database.dao.ChatDao
import com.example.wire.core.database.dao.MessageDao
import com.example.wire.core.database.entity.ChatEntity
import com.example.wire.core.di.ApplicationScope
import com.example.wire.core.network.notification.NotificationHandler
import com.example.wire.core.network.websocket.WebSocketManager
import com.example.wire.feature.chat.data.mapper.toEntity
import com.example.wire.feature.chat.data.remote.dto.ChatActionDto
import com.example.wire.feature.notifications.domain.model.NotificationType
import com.example.wire.feature.notifications.domain.model.WireNotification
import com.example.wire.feature.notifications.domain.repository.NotificationRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.util.UUID
import javax.inject.Inject

class ObserveWebSocketEventsUseCase @Inject constructor(
    private val webSocketManager: WebSocketManager,
    private val messageDao: MessageDao,
    private val chatDao: ChatDao,
    private val notificationRepository: NotificationRepository,
    private val notificationHandler: NotificationHandler,
    @field:ApplicationScope private val scope: CoroutineScope
) {
    operator fun invoke() {
        scope.launch {
            webSocketManager.observeMessages().collect { jsonString ->
                try {
                    val chatAction = Json.decodeFromString<ChatActionDto>(jsonString)
                    
                    when(chatAction.action) {
                        "SEND" -> handleIncomingMessage(chatAction)
                        "PAYMENT" -> handleIncomingPayment(chatAction)
                        "CONTACT_MATCH" -> handleContactMatch(chatAction)
                    }
                } catch (e: Exception) {
                    println("Websocket Error: ${e.message}")
                }
            }
        }
    }

    private suspend fun handleIncomingMessage(action: ChatActionDto) {
        val msg = action.message ?: return
        
        // 1. Save to Room
        messageDao.insertMessage(msg.toEntity(chatId = msg.senderId))
        chatDao.updateChatPreview(
            chatId = msg.senderId,
            lastMessage = msg.content,
            timestamp = System.currentTimeMillis()
        )

        // 2. Show Alert
        notificationHandler.showSystemAlert(
            title = msg.metadata?.get("senderName") ?: "New Message",
            message = msg.content,
            type = NotificationType.MESSAGE
        )
    }

    private suspend fun handleIncomingPayment(action: ChatActionDto) {
        val msg = action.message ?: return
        
        val notification = WireNotification(
            id = msg.id ?: UUID.randomUUID().toString(),
            title = "Payment Received",
            content = "You received $${msg.content} from ${msg.senderId}",
            type = NotificationType.PAYMENT_RECEIVED,
            timestamp = System.currentTimeMillis(),
            isRead = false
        )
        
        notificationRepository.saveNotification(notification)
        
        notificationHandler.showSystemAlert(
            title = "Money Received! 💰",
            message = notification.content,
            type = NotificationType.PAYMENT_RECEIVED
        )
    }

    private suspend fun handleContactMatch(action: ChatActionDto) {
        val matchedUser = action.message ?: return
        val name = matchedUser.metadata?.get("senderName") ?: "New Contact"
        
        chatDao.upsertChat(
            ChatEntity(
                chatId = matchedUser.senderId,
                contactName = name,
                lastMessage = "Recently joined Wire",
                timestamp = System.currentTimeMillis(),
                avatarColor = AvatarUtils.getColorForName(name),
                isContact = true
            )
        )
    }
}
