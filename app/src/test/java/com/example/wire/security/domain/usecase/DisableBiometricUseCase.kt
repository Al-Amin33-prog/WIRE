package com.example.wire.security.domain.usecase

import com.example.wire.core.feature.security.domain.repository.SecurityRepository
import com.example.wire.core.feature.security.domain.usecase.DisableBiometricUseCase

import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DisableBiometricUseCaseTest {

    private val repository = mockk<SecurityRepository>()

    private lateinit var useCase: DisableBiometricUseCase

    @Before
    fun setup() {
        useCase = DisableBiometricUseCase(repository)
    }

    @Test
    fun `should disable biometric`() = runTest {

        coEvery {
            repository.disableBiometric()
        } just Runs

        useCase(Unit)

        coVerify {
            repository.disableBiometric()
        }
    }
}