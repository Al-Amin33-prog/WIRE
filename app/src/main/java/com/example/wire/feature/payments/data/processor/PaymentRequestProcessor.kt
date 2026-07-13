package com.example.wire.feature.payments.data.processor

import com.example.wire.core.network.notification.NotificationHandler
import com.example.wire.core.network.websocket.WebSocketProcessor
import com.example.wire.feature.chat.data.remote.dto.ChatActionDto
import java.util.UUID
import javax.inject.Inject

class PaymentRequestProcessor @Inject constructor(
    private val notificationHandler: NotificationHandler
) : WebSocketProcessor {
    override val action = "REQUEST_PAYMENT"

    override suspend fun process(actionDto: ChatActionDto) {
        val msg = actionDto.message ?: return

        // This triggers the Android System notification
        notificationHandler.showPaymentRequestNotification(
            requestId = msg.id ?: UUID.randomUUID().toString(),
            senderName = msg.metadata?.get("senderName") ?: "Someone",
            amount = msg.content
        )
    }
}