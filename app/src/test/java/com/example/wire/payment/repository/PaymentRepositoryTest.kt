package com.example.wire.payment.repository

import com.example.wire.core.common.util.PerformanceMonitor
import com.example.wire.core.database.dao.TransactionDao
import com.example.wire.core.domain.dispatcher.CoroutineDispatchers // Ensure this is the right interface
import com.example.wire.feature.payments.data.remote.PaymentApiService
import com.example.wire.feature.payments.data.repository.PaymentRepositoryImpl
import com.example.wire.feature.payments.data.util.IdempotencyKeyGenerator
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class PaymentRepositoryTest {

    private val transactionDao = mockk<TransactionDao>(relaxed = true)
    private val api = mockk<PaymentApiService>(relaxed = true)
    private val generator = mockk<IdempotencyKeyGenerator>()
    private val performanceMonitor = mockk<PerformanceMonitor>(relaxed = true)

    private val testDispatcher = UnconfinedTestDispatcher()

    // THE FIX: Since CoroutineDispatchers is an interface, we create an anonymous object
    private val dispatchers = object : CoroutineDispatchers {
        override val main = testDispatcher
        override val io = testDispatcher
        override val default = testDispatcher
    }

    private lateinit var repository: PaymentRepositoryImpl

    @Before
    fun setup() {
        repository = PaymentRepositoryImpl(
            api = api,
            transactionDao = transactionDao,
            performanceMonitor = performanceMonitor,
            idempotencyKeyGenerator = generator,
            dispatchers = dispatchers
        )
    }

    @Test
    fun `createPaymentIntent should anchor PENDING transaction in Room before API call`() = runTest {
        // Arrange
        val testKey = "test-key-123"
        coEvery { generator.generate() } returns testKey

        // Act
        repository.createPaymentIntent(75.0, "user_sarah", "Dinner")

        // Assert
        coVerify {
            transactionDao.upsertTransactions(match { list ->
                val tx = list[0]
                tx.id == testKey && tx.status == "PENDING" && tx.amount == 75.0
            })
        }
    }
}