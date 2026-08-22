package com.example.wire.security.domain.usecase

import app.cash.turbine.test
import com.example.wire.core.feature.security.domain.model.SecuritySettings
import com.example.wire.core.feature.security.domain.usecase.*
import com.example.wire.core.feature.security.presentation.SecurityViewModel
import com.example.wire.core.feature.security.presentation.event.SecurityUiEvent
import com.example.wire.core.feature.security.presentation.state.SecurityStep
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SecurityViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    
    private lateinit var viewModel: SecurityViewModel
    private lateinit var useCases: SecurityUseCases
    
    private val createPinUseCase = mockk<CreatePinUseCase>(relaxed = true)
    private val verifyPinUseCase = mockk<VerifyPinUseCase>(relaxed = true)
    private val hasPinUseCase = mockk<HasPinUseCase>(relaxed = true)
    private val enableBiometricUseCase = mockk<EnableBiometricUseCase>(relaxed = true)
    private val disableBiometricUseCase = mockk<DisableBiometricUseCase>(relaxed = true)
    private val getSecuritySettingsUseCase = mockk<GetSecuritySettingsUseCase>(relaxed = true)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        
        useCases = SecurityUseCases(
            createPin = createPinUseCase,
            verifyPin = verifyPinUseCase,
            hasPin = hasPinUseCase,
            enableBiometric = enableBiometricUseCase,
            disableBiometric = disableBiometricUseCase,
            getSecuritySettings = getSecuritySettingsUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init sets SetPin step if no PIN exists`() = runTest {
        coEvery { getSecuritySettingsUseCase(
            Unit
        ) } returns SecuritySettings(
            hasPin = false,
            isBiometricEnabled = false
        )
        
        viewModel = SecurityViewModel(useCases)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(SecurityStep.SetPin, state.step)
        }
    }

    @Test
    fun `Successful PIN confirmation transitions to Biometric Enrollment`() = runTest {
        coEvery { getSecuritySettingsUseCase(Unit) } returnsMany listOf(
            SecuritySettings(
                hasPin = false,
                isBiometricEnabled = false
            ),
            SecuritySettings(hasPin = true,  isBiometricEnabled = false)
        )
        
        viewModel = SecurityViewModel(useCases)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(SecurityUiEvent.PinChanged("1234"))
        viewModel.onEvent(SecurityUiEvent.CreatePinClicked) // Moves to ConfirmPin
        viewModel.onEvent(SecurityUiEvent.ConfirmPinChanged("1234"))
        viewModel.onEvent(SecurityUiEvent.CreatePinClicked) // Completes

        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(SecurityStep.EnrollBiometric, state.step)
            coVerify { createPinUseCase("1234") }
        }
    }

    @Test
    fun `Mismatched PINs shows validation error`() = runTest {
        coEvery { getSecuritySettingsUseCase(Unit) } returns SecuritySettings(
            hasPin = false,
            isBiometricEnabled = false)
        viewModel = SecurityViewModel(useCases)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(SecurityUiEvent.PinChanged("1234"))
        viewModel.onEvent(SecurityUiEvent.CreatePinClicked)
        viewModel.onEvent(SecurityUiEvent.ConfirmPinChanged("9999"))
        viewModel.onEvent(SecurityUiEvent.CreatePinClicked)

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals("PINs do not match", state.pinError)
        }
    }
}
