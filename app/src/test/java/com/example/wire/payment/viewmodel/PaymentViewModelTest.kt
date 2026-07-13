package com.example.wire.payment.viewmodel

import androidx.fragment.app.FragmentActivity
import com.example.wire.core.common.util.PerformanceMonitor
import com.example.wire.core.datastore.preferences.UserPreferencesDataStore
import com.example.wire.core.ui.util.WireBiometricManager
import com.example.wire.feature.auth.domain.repository.AuthRepository
import com.example.wire.feature.payments.domain.usecase.PaymentUseCases
import com.example.wire.feature.payments.presentation.PaymentViewModel
import com.example.wire.feature.payments.presentation.event.PaymentUiEvent
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class PaymentViewModelTest {
    private val paymentUseCases = mockk<PaymentUseCases>(relaxed = true)
    private val authRepository = mockk<AuthRepository>(relaxed = true)
    private val biometricManager = mockk<WireBiometricManager>(relaxed = true)
    private val dataStore = mockk<UserPreferencesDataStore>(relaxed = true)
    private val performanceMonitor = mockk<PerformanceMonitor>(relaxed = true)

    private lateinit var viewModel: PaymentViewModel

    @Before
    fun setup() {
        viewModel = PaymentViewModel(
            paymentUseCases, authRepository, performanceMonitor, biometricManager, dataStore
        )
    }

    @Test
    fun `ConfirmClicked event should trigger Biometric Prompt when security is enabled`() = runTest {
        // Arrange: User has enabled biometrics in settings
        every { dataStore.isBiometricEnabled } returns flowOf(true)
        val activity = mockk<FragmentActivity>()

        // Act: User clicks the "Confirm & Send" button
        viewModel.onEvent(PaymentUiEvent.ConfirmClicked, activity)

        // Assert: The Biometric Dialog MUST be shown to the user
        verify {
            biometricManager.showBiometricPrompt(
                activity = activity,
                title = "Confirm Payment",
                any(), any(), any(), any()
            )
        }
    }
}