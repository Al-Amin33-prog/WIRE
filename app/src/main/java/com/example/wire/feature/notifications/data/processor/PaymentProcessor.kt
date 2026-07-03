package com.example.wire.feature.notifications.data.processor

import com.example.wire.core.network.notification.NotificationHandler
import com.example.wire.core.network.websocket.WebSocketProcessor
import com.example.wire.feature.chat.data.remote.dto.ChatActionDto
import com.example.wire.feature.notifications.domain.model.NotificationType
import com.example.wire.feature.notifications.domain.model.WireNotification
import com.example.wire.feature.notifications.domain.repository.NotificationRepository
import java.util.UUID
import javax.inject.Inject

class PaymentProcessor @Inject constructor(
    private val notificationRepository: NotificationRepository,
    private val notificationHandler: NotificationHandler
) : WebSocketProcessor {
    override val action = "PAYMENT"

    override suspend fun process(actionDto: ChatActionDto) {
        val msg = actionDto.message ?: return
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
}