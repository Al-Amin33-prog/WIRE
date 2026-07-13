package com.example.wire.payment.processor

import com.example.wire.core.database.dao.TransactionDao
import com.example.wire.feature.chat.data.mapper.toDomain
import com.example.wire.feature.chat.data.remote.dto.ChatActionDto
import com.example.wire.feature.chat.data.remote.dto.MessageDto
import com.example.wire.feature.payments.data.processor.PaymentProcessor
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test

class LedgerPaymentProcessorTest {

    private val transactionDao = mockk<TransactionDao>(relaxed = true)
    private val processor = PaymentProcessor(transactionDao)

    @Test
    fun `process should upsert transaction into ledger when action is PAYMENT`() = runBlocking {
        // 1. Create the DTO
        val messageDto = MessageDto(
            id = "tx_999",
            senderId = "user_sarah",
            content = "150.0",
            timestamp = 123456789L,
            type = "PAYMENT",
            metadata = mapOf("senderName" to "Sarah K.", "note" to "For Rent")
        )

        // 2. Wrap it in the ChatActionDto
        // FIX: Ensure 'message' property in ChatActionDto accepts MessageDto
        val paymentAction = ChatActionDto(
            action = "PAYMENT",
            message = messageDto.toDomain()
        )

        // 3. Execute
        processor.process(paymentAction)

        // 4. Verify the "Accountant" logic
        coVerify {
            transactionDao.upsertTransactions(match { list ->
                val tx = list[0]
                tx.id == "tx_999" && tx.amount == 150.0 && tx.type == "RECEIVE"
            })
        }
    }
}