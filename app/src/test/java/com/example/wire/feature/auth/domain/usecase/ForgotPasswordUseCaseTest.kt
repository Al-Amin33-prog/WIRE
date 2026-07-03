package com.example.wire.feature.auth.domain.usecase

import com.example.wire.core.common.util.AppError
import com.example.wire.core.common.util.Resource
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
    fun `invoke with invalid email format returns validation error`() = runBlocking {
        val result = useCase("invalid-email")

        assertTrue(result is Resource.Error)
        val error = (result as Resource.Error).error
        assertTrue(error is AppError.Validation)
        assertEquals("Please enter a valid email address", (error as AppError.Validation).message)
    }

    @Test
    fun `invoke with valid email returns success resource`() = runBlocking {
        coEvery { repository.sendPasswordResetEmail(any()) } returns Result.success(Unit)

        val result = useCase("test@example.com")

        assertTrue(result is Resource.Success)
    }
}