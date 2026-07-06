package com.example.wire.feature.auth.domain.usecase

import com.example.wire.core.common.util.AppError
import com.example.wire.core.common.util.Resource
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
    fun `invoke with short password returns validation error`() = runBlocking {
        val params = CreateAccountUseCase.Params("test@example.com", "1234567", "Test User", "123456789")

        val result = useCase(params)

        assertTrue(result is Resource.Error)
        val error = (result as Resource.Error).error
        assertTrue(error is AppError.Validation)
        assertEquals("Password must be at least 8 characters", (error as AppError.Validation).message)
    }

    @Test
    fun `invoke with empty display name returns validation error`() = runBlocking {
        val params = CreateAccountUseCase.Params("test@example.com", "password123", "", "12345678")

        val result = useCase(params)

        assertTrue(result is Resource.Error)
        val error = (result as Resource.Error).error
        assertTrue(error is AppError.Validation)
        assertEquals("Display name cannot be empty", (error as AppError.Validation).message)
    }

    @Test
    fun `invoke with valid data returns success resource`() = runBlocking {
        val email = "test@example.com"
        val user = AuthUser(
            uid = "123",
            email = email,
            displayName = "Test User",
            isEmailVerified = false,
            token = "token",
            phone = "12345678"
        )

        // FIX: Mocking the repository to return the new Resource type
        coEvery { repository.register(any(), any(), any(), any()) } returns Resource.Success(user)

        val params = CreateAccountUseCase.Params(email, "password123", "Test User", "12345678")
        val result = useCase(params)

        assertTrue(result is Resource.Success)
        assertEquals(user, (result as Resource.Success).data)
    }

    @Test
    fun `invoke with repository failure returns network unknown error`() = runBlocking {
        // FIX: Mocking the repository to return Resource.Error directly
        val errorMessage = "Email taken"
        coEvery { repository.register(any(), any(), any(), any()) } returns
                Resource.Error(AppError.Network.Unknown(errorMessage))

        val params = CreateAccountUseCase.Params("test@example.com", "password123", "Test User", "12345678")
        val result = useCase(params)

        assertTrue(result is Resource.Error)
        val error = (result as Resource.Error).error
        assertTrue(error is AppError.Network.Unknown)
        assertEquals(errorMessage, (error as AppError.Network.Unknown).message)
    }

}