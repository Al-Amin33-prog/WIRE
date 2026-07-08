package com.example.wire.feature.auth.domain.usecase

import app.cash.turbine.test
import com.example.wire.core.common.util.Resource
import com.example.wire.core.common.util.Resource.Success
import com.example.wire.core.datastore.preferences.UserPreferencesDataStore
import com.example.wire.core.ui.util.WireBiometricManager
import com.example.wire.feature.auth.domain.model.AuthUser
import com.example.wire.feature.auth.presentation.AuthViewModel
import com.example.wire.feature.auth.presentation.event.AuthUiEvent
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {

    // Dependencies
    private val authUseCases = mockk<AuthUseCases>(relaxed = true)
    private val biometricManager = mockk<WireBiometricManager>()
    private val dataStore = mockk<UserPreferencesDataStore>(relaxed = true)

    private lateinit var viewModel: AuthViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        // Default hardware/pref states
        every { biometricManager.isBiometricAvailable() } returns true
        every { dataStore.isBiometricEnabled } returns flowOf(false)

        viewModel = AuthViewModel(
            authUseCases = authUseCases,
            biometricManager = biometricManager,
            userPreferencesDataStore = dataStore
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `successful login should trigger Enrollment Sheet if hardware exists and not enrolled`() = runTest {
        // Arrange: Mock a successful login result
        val email = "test@example.wire"
       // coEvery { authUseCases.login(any()) } returns Resource<AuthUser>

        // Act: Set email/password and click login
        viewModel.onEvent(AuthUiEvent.EmailChanged(email))
        viewModel.onEvent(AuthUiEvent.PasswordChanged("password123"))
        viewModel.onEvent(AuthUiEvent.LoginClicked)

        // Wait for the coroutine inside ViewModel to finish
        advanceUntilIdle()

        // Assert: Use Turbine to check the resulting state
        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue("Enrollment sheet should be visible", state.showBiometricEnrollment)
            assertFalse("User should not be fully logged in until enrollment is handled", state.isLoggedIn)
        }
    }
}