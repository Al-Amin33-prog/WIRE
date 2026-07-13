package com.example.wire.feature.payments.data.repository


import com.example.wire.core.common.util.AppError
import com.example.wire.core.common.util.PerformanceMonitor
import com.example.wire.core.common.util.Resource
import com.example.wire.core.database.dao.TransactionDao
import com.example.wire.core.database.entity.TransactionEntity
import com.example.wire.core.domain.dispatcher.CoroutineDispatchers
import com.example.wire.feature.payments.data.remote.PaymentApiService
import com.example.wire.feature.payments.data.remote.StripeApiService // Added
import com.example.wire.feature.payments.data.remote.dto.*
import com.example.wire.feature.payments.data.util.IdempotencyKeyGenerator
import com.example.wire.feature.payments.domain.model.PaymentIntent
import com.example.wire.feature.payments.domain.repository.PaymentRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PaymentRepositoryImpl @Inject constructor(
    private val api: PaymentApiService,
    private val stripeApiService: StripeApiService, // THE FIX: Inject the Stripe Service
    private val transactionDao: TransactionDao,
    private val performanceMonitor: PerformanceMonitor,
    private val idempotencyKeyGenerator: IdempotencyKeyGenerator,
    private val dispatchers: CoroutineDispatchers
) : PaymentRepository {

    override suspend fun createPaymentIntent(
        amount: Double,
        recipientId: String,
        note: String?,
    ): Resource<PaymentIntent> = withContext(dispatchers.io) {
        val startTime = System.currentTimeMillis()
        val idempotencyKey = idempotencyKeyGenerator.generate()

        try {
            // 1. Fetch Stripe Customer Context (Ephemeral Key)
            // This enables saved cards and returning customer features
            val stripeContext = stripeApiService.getStripeCustomerContext()

            // 2. Create the Payment Intent via our Ktor Backend
            val response = api.createPaymentIntent(
                CreatePaymentRequest(
                    amount = amount,
                    recipientId = recipientId,
                    note = note,
                    idempotencyKey = idempotencyKey
                )
            )

            // 3. Anchor in Room (SSOT)
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

            // 4. Return combined Domain model
            Resource.Success(PaymentIntent(
                clientSecret = response.clientSecret,
                publishableKey = response.publishableKey,
                customerId = stripeContext.customerId,       // Passed to PaymentSheet
                ephemeralKeySecret = stripeContext.ephemeralKey // Passed to PaymentSheet
            ))
        } catch (e: Exception) {
            Resource.Error(AppError.Network.Unknown(e.message))
        }
    }

    override suspend fun confirmPayment(paymentId: String, status: String): Resource<Unit> =
        withContext(dispatchers.io) { // THE FIX: Use injected dispatchers, not hardcoded IO
            try {
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

    override suspend fun getPaymentStatus(paymentId: String): Resource<String> =
        withContext(dispatchers.io) {
            try {
                val status = api.getPaymentStatus(paymentId)
                Resource.Success(status)
            } catch (e: Exception) {
                Resource.Error(AppError.Network.Unknown(e.message))
            }
        }
}