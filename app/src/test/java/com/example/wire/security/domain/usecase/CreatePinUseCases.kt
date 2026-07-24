package com.example.wire.security.domain.usecase

import com.example.wire.core.feature.security.domain.repository.SecurityRepository
import com.example.wire.core.feature.security.domain.usecase.CreatePinUseCase
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
class CreatePinUseCaseTest {

    private val repository = mockk<SecurityRepository>()

    private lateinit var useCase: CreatePinUseCase

    @Before
    fun setup() {
        useCase = CreatePinUseCase(repository)
    }

    @Test
    fun `should create pin`() = runTest {

        coEvery {
            repository.createPin("1234")
        } just Runs

        useCase("1234")

        coVerify(exactly = 1) {
            repository.createPin("1234")
        }
    }
}