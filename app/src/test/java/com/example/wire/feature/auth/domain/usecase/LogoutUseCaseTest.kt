package com.example.wire.feature.auth.domain.usecase

import com.example.wire.feature.auth.domain.repository.AuthRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test

class LogoutUseCaseTest {

    private val repository = mockk<AuthRepository>(relaxed = true)
    private val useCase = LogoutUseCase(repository)

    @Test
    fun `invoke calls repository logout`() = runBlocking {
        useCase(Unit)

        coVerify { repository.logout() }
    }
}
