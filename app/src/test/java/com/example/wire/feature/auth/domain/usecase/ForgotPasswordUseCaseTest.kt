package com.example.wire.feature.auth.domain.usecase

import com.example.wire.feature.auth.domain.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ForgotPasswordUseCaseTest {

    private val repository = mockk<AuthRepository>()
    private val useCase = ForgotPasswordUseCase(repository)

    @Test
    fun `invoke with blank email returns failure`() = runBlocking {
        val result = useCase("")

        assertTrue(result.isFailure)
        assertEquals("Please enter a valid email", result.exceptionOrNull()?.message)
    }

    @Test
    fun `invoke with invalid email format returns failure`() = runBlocking {
        val result = useCase("invalid-email")

        assertTrue(result.isFailure)
        assertEquals("Please enter a valid email", result.exceptionOrNull()?.message)
    }

    @Test
    fun `invoke with valid email returns success from repository`() = runBlocking {
        val email = "test@example.com"
        coEvery { repository.sendPasswordResetEmail(email) } returns Result.success(Unit)

        val result = useCase(email)

        assertTrue(result.isSuccess)
    }

    @Test
    fun `invoke with repository error returns failure`() = runBlocking {
        val email = "test@example.com"
        coEvery { repository.sendPasswordResetEmail(email) } returns Result.failure(Exception("Network error"))

        val result = useCase(email)

        assertTrue(result.isFailure)
        assertEquals("Network error", result.exceptionOrNull()?.message)
    }
}
