package com.example.wire.feature.payments.data.repository

import com.example.wire.core.common.util.AppError
import com.example.wire.core.common.util.PerformanceMonitor
import com.example.wire.core.common.util.Resource
import com.example.wire.core.database.dao.TransactionDao
import com.example.wire.core.database.entity.TransactionEntity
import com.example.wire.feature.payments.data.remote.PaymentApiService
import com.example.wire.feature.payments.data.remote.dto.*
import com.example.wire.feature.payments.data.util.IdempotencyKeyGenerator
import com.example.wire.feature.payments.domain.model.PaymentIntent
import com.example.wire.feature.payments.domain.repository.PaymentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PaymentRepositoryImpl @Inject constructor(
    private val api: PaymentApiService,
    private val transactionDao: TransactionDao,
    private val performanceMonitor: PerformanceMonitor,
    private val idempotencyKeyGenerator: IdempotencyKeyGenerator // Standardized naming
) : PaymentRepository {

    override suspend fun createPaymentIntent(
        amount: Double,
        recipientId: String,
        note: String?,

    ): Resource<PaymentIntent> = withContext(Dispatchers.IO) {
        val startTime = System.currentTimeMillis()

        // 1. Generate the key
        val idempotencyKey = idempotencyKeyGenerator.generate()

        try {
            // 2. Network call with all 4 parameters
            val response = api.createPaymentIntent(
                CreatePaymentRequest(
                    amount = amount,
                    recipientId = recipientId,
                    note = note,
                    idempotencyKey = idempotencyKey
                )
            )

            // 3. Anchor in Room (SSOT)
            // Ensure these names match your TransactionEntity exactly
            transactionDao.upsertTransactions(listOf(
                TransactionEntity(
                    id = idempotencyKey,
                    amount = amount,
                    type = "SEND",
                    status = "PENDING",
                    timestamp = System.currentTimeMillis(),
                    note = note,
                    counterPartyName = "Recipient",
                    counterPartyId = recipientId
                )
            ))

            performanceMonitor.recordEvent(System.currentTimeMillis() - startTime)

            Resource.Success(PaymentIntent(
                clientSecret = response.clientSecret,
                publishableKey = response.publishableKey
            ))
        } catch (e: Exception) {
            Resource.Error(AppError.Network.Unknown(e.message))
        }
    }

    override suspend fun confirmPayment(paymentId: String, status: String): Resource<Unit> =
        withContext(Dispatchers.IO) {
            try {
                // FIXED: Passing named arguments to match the updated DTO
                api.confirmPayment(
                    ConfirmPaymentRequest(
                        paymentId = paymentId,
                        status = status,
                        note = null
                    )
                )
                Resource.Success(Unit)
            } catch (e: Exception) {
                Resource.Error(AppError.Network.Unknown(e.message))
            }
        }

    override suspend fun getPaymentStatus(paymentId: String): Resource<String> {
        return try {
            val status = api.getPaymentStatus(paymentId)
            Resource.Success(status)
        } catch (e: Exception) {
            Resource.Error(AppError.Network.Unknown(e.message))
        }
    }
}