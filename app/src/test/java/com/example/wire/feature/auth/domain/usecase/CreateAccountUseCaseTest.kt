package com.example.wire.feature.auth.domain.usecase

import com.example.wire.feature.auth.domain.model.AuthUser
import com.example.wire.feature.auth.domain.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CreateAccountUseCaseTest {

    private val repository = mockk<AuthRepository>()
    private val useCase = CreateAccountUseCase(repository)

    @Test
    fun `invoke with short password returns failure`() = runBlocking {
        val params = CreateAccountUseCase.Params("test@example.com", "1234567", "Test User")
        val result = useCase(params)

        assertTrue(result.isFailure)
        assertEquals("Password must be at least 8 characters", result.exceptionOrNull()?.message)
    }

    @Test
    fun `invoke with empty display name returns failure`() = runBlocking {
        val params = CreateAccountUseCase.Params("test@example.com", "password123", "")
        val result = useCase(params)

        assertTrue(result.isFailure)
        assertEquals("Display name cannot be empty", result.exceptionOrNull()?.message)
    }

    @Test
    fun `invoke with valid data returns success from repository`() = runBlocking {
        val email = "test@example.com"
        val password = "password123"
        val displayName = "Test User"
        val user = AuthUser(uid = "123", email = email, displayName = displayName, token = "token", isEmailVerified = false)

        coEvery { repository.register(email, password, displayName) } returns Result.success(user)

        val params = CreateAccountUseCase.Params(email, password, displayName)
        val result = useCase(params)

        assertTrue(result.isSuccess)
        assertEquals(user, result.getOrNull())
    }

    @Test
    fun `invoke with existing email returns failure from repository`() = runBlocking {
        val email = "existing@example.com"
        val password = "password123"
        val displayName = "Test User"

        coEvery { repository.register(email, password, displayName) } returns Result.failure(Exception("Email already in use"))

        val params = CreateAccountUseCase.Params(email, password, displayName)
        val result = useCase(params)

        assertTrue(result.isFailure)
        assertEquals("Email already in use", result.exceptionOrNull()?.message)
    }
}
