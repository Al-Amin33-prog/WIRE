package com.example.wire.security.domain.usecase


import com.example.wire.core.feature.security.domain.repository.SecurityRepository
import com.example.wire.core.feature.security.domain.usecase.EnableBiometricUseCase
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

class EnableBiometricUseCaseTest {

    private val repository = mockk<SecurityRepository>()

    private lateinit var useCase: EnableBiometricUseCase

    @Before
    fun setup() {
        useCase = EnableBiometricUseCase(repository)
    }

    @Test
    fun `should enable biometric`() = runTest {

        coEvery {
            repository.enableBiometric()
        } just Runs

        useCase()

        coVerify {
            repository.enableBiometric()
        }
    }
}