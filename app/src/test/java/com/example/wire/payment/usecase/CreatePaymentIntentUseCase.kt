package com.example.wire.payment.usecase



import com.example.wire.core.common.util.AppError
import com.example.wire.core.common.util.Resource
import com.example.wire.feature.payments.domain.model.PaymentIntent
import com.example.wire.feature.payments.domain.repository.PaymentRepository
import com.example.wire.feature.payments.domain.usecase.CreatePaymentIntentUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CreatePaymentIntentUseCaseTest {

    private val repository = mockk<PaymentRepository>()
    private val useCase = CreatePaymentIntentUseCase(repository)

    @Test
    fun `invoke with zero or negative amount returns validation error`() = runBlocking {
        val result = useCase(amount = -10.0, recipientId = "user_1", note = "Test")

        assertTrue(result is Resource.Error)
        val error = (result as Resource.Error).error
        assertTrue(error is AppError.Validation)
        assertEquals("Amount must be greater than zero", (error as AppError.Validation).message)
    }

    @Test
    fun `invoke with valid data calls repository and returns success`() = runBlocking {
        val mockIntent = PaymentIntent("client_secret_123", "pk_test_456")

        coEvery {
            repository.createPaymentIntent(any(), any(), any())
        } returns Resource.Success(mockIntent)

        val result = useCase(amount = 50.0, recipientId = "user_1", note = "Dinner")

        assertTrue(result is Resource.Success)
        assertEquals("client_secret_123", (result as Resource.Success).data.clientSecret)
    }
}