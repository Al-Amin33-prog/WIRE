package com.example.wire.feature.payments.data.processor



import com.example.wire.core.database.dao.TransactionDao
import com.example.wire.core.database.entity.TransactionEntity
import com.example.wire.core.network.websocket.WebSocketProcessor
import com.example.wire.feature.chat.data.remote.dto.ChatActionDto
import java.util.UUID
import javax.inject.Inject

class PaymentProcessor @Inject constructor(
    private val transactionDao: TransactionDao
) : WebSocketProcessor {
    override val action = "PAYMENT"

    override suspend fun process(actionDto: ChatActionDto) {
        val msg = actionDto.message ?: return

        // When a payment arrives, we save it to our Ledger (Transaction Table)
        transactionDao.upsertTransactions(listOf(
            TransactionEntity(
                id = msg.id ?: UUID.randomUUID().toString(),
                amount = msg.content.toDoubleOrNull() ?: 0.0,
                type = "RECEIVE",
                status = "COMPLETED",
                timestamp = msg.timestamp,
                note = msg.metadata?.get("note"),
                counterPartyName = msg.metadata?.get("senderName") ?: "Unknown",
                counterPartyId = msg.senderId
            )
        ))
    }
}