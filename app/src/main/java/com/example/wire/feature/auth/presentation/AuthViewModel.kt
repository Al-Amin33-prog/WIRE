package com.example.wire.feature.auth.presentation

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wire.core.common.util.AppError
import com.example.wire.core.common.util.Resource
import com.example.wire.core.datastore.preferences.UserPreferencesDataStore
import com.example.wire.core.ui.util.WireBiometricManager
import com.example.wire.feature.auth.domain.usecase.*
import com.example.wire.feature.auth.presentation.event.AuthUiEvent
import com.example.wire.feature.auth.presentation.state.AuthUiState
import com.example.wire.feature.chat.data.wrapper.ChatUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUseCases: AuthUseCases,
    private val biometricManager: WireBiometricManager,
    private val userPreferencesDataStore: UserPreferencesDataStore,
    private val chatUseCases: ChatUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        observeAuthState()
        checkBiometricAvailability()
        checkBiometricButtonVisibility()
    }

    private fun checkBiometricAvailability() {
        _uiState.update { it.copy(isBiometricAvailable = biometricManager.isBiometricAvailable()) }
    }

    fun onEvent(event: AuthUiEvent) {
        when (event) {
            is AuthUiEvent.EmailChanged -> _uiState.update { it.copy(email = event.value) }
            is AuthUiEvent.PasswordChanged -> _uiState.update { it.copy(password = event.value) }
            is AuthUiEvent.ConfirmPasswordChanged -> _uiState.update { it.copy(confirmPassword = event.value) }
            is AuthUiEvent.DisplayNameChanged -> _uiState.update { it.copy(displayName = event.value) }
            is AuthUiEvent.OnPhoneChange -> _uiState.update { it.copy(phone = event.phone) }

            AuthUiEvent.LoginClicked -> login()
            AuthUiEvent.CreateAccountClicked -> createAccount()
            AuthUiEvent.LogoutClicked -> logout()
            AuthUiEvent.ForgotPasswordClicked -> sendPasswordReset()

            AuthUiEvent.BiometricLoginClicked -> _uiState.update { it.copy(showBiometricPrompt = true) }
            AuthUiEvent.BiometricAuthSucceeded -> {
                _uiState.update { it.copy(showBiometricPrompt = false) }
            }
            is AuthUiEvent.BiometricAuthFailed -> {
                _uiState.update { it.copy(showBiometricPrompt = false, errorMessage = event.reason) }
            }

            AuthUiEvent.GoogleSignInClicked -> _uiState.update { it.copy(triggerGoogleSignIn = true) }
            is AuthUiEvent.GoogleSignInResult -> handleGoogleSignIn(event.idToken)
            is AuthUiEvent.GoogleSignInFailed -> _uiState.update {
                it.copy(errorMessage = event.reason, triggerGoogleSignIn = false)
            }
            is AuthUiEvent.EnrollBiometrics -> {
                viewModelScope.launch {
                    userPreferencesDataStore.setBiometricEnabled(event.value)
                    _uiState.update { it.copy(isBiometricEnabled = event.value) }
                     _uiState.update { it.copy(
                         showBiometricEnrollment = false,
                         isLoggedIn = true
                     ) }
                }
            }
        }
    }

    private fun handleGoogleSignIn(idToken: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, triggerGoogleSignIn = false) }
            // FIX: Access through authUseCases
            when (val result = authUseCases.googleSignIn(idToken)) {
                is Resource.Success -> {
                    userPreferencesDataStore.setLoggedIn(true)
                    _uiState.update { it.copy(isLoading = false, isLoggedIn = true) }
                    val user = authUseCases.getCurrentUser()
                    val email = user?.email ?: ""
                    handleSuccessfulAuth(email)
                }
                is Resource.Error -> {
                    _uiState.update { it.copy(isLoading = false, errorMessage = mapError(result.error)) }
                }
                is Resource.Loading -> { }
            }
        }
    }

    private fun login() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val params = LoginUseCase.Params(_uiState.value.email, _uiState.value.password)

            // FIX: Access through authUseCases
            when (val result = authUseCases.login(params)) {
                is Resource.Success -> {
                    userPreferencesDataStore.setLoggedIn(true)
                    _uiState.update { it.copy(isLoading = false, isLoggedIn = true) }
                    handleSuccessfulAuth(_uiState.value.email)
                }
                is Resource.Error -> {
                    _uiState.update { it.copy(isLoading = false, errorMessage = mapError(result.error)) }
                }
                is Resource.Loading -> { }
            }
        }
    }

    private fun createAccount() {
        viewModelScope.launch {
            val current = _uiState.value
            if (current.password != current.confirmPassword) {
                _uiState.update { it.copy(errorMessage = "Passwords do not match") }
                return@launch
            }
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val params = CreateAccountUseCase.Params(
                current.email, current.password, current.displayName, current.phone
            )

            // FIX: Access through authUseCases
            when (val result = authUseCases.createAccount(params)) {
                is Resource.Success -> {
                    userPreferencesDataStore.setLoggedIn(true)
                    _uiState.update { it.copy(isLoading = false, isLoggedIn = true) }
                    handleSuccessfulAuth(_uiState.value.email)
                }
                is Resource.Error -> {
                    _uiState.update { it.copy(isLoading = false, errorMessage = mapError(result.error)) }
                }
                is Resource.Loading -> { }
            }
        }
    }

    private fun sendPasswordReset() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            // FIX: Access through authUseCases
            when (val result = authUseCases.forgotPassword(_uiState.value.email)) {
                is Resource.Success -> {
                    _uiState.update { it.copy(isLoading = false, isPasswordResetEmailSent = true) }
                }
                is Resource.Error -> {
                    _uiState.update { it.copy(isLoading = false, errorMessage = mapError(result.error)) }
                }
                is Resource.Loading -> { }
            }
        }
    }

    private fun logout() {
        viewModelScope.launch {
            // FIX: Access through authUseCases
            authUseCases.logout(Unit)
            userPreferencesDataStore.clearAll()
            chatUseCases.disconnectFromChat()
            _uiState.update { AuthUiState() }
        }
    }

    private fun mapError(error: AppError): String {
        return when (error) {
            is AppError.Validation -> error.message
            is AppError.Network.NoInternet -> "No internet connection. Please check your network."
            is AppError.Network.Timeout -> "Request timed out. Please try again."
            is AppError.Network.Unknown -> error.message ?: "An unexpected error occurred"
            else -> "An error occurred"
        }
    }

    private fun observeAuthState() {
        viewModelScope.launch {
            // FIX: Access through authUseCases
            authUseCases.observeAuthState(Unit).collect { user ->
                _uiState.update { it.copy(isLoggedIn = user != null) }
            }
        }
    }

    fun showBiometricPrompt(activity: FragmentActivity) {
        biometricManager.showBiometricPrompt(
            activity = activity,
            onSuccess = { onEvent(AuthUiEvent.BiometricAuthSucceeded) },
            onError = { onEvent(AuthUiEvent.BiometricAuthFailed(it)) },
            onFailed = { onEvent(AuthUiEvent.BiometricAuthFailed("Not recognized")) }
        )
    }



    private fun handleSuccessfulAuth(email: String) {
        viewModelScope.launch {userPreferencesDataStore.setSavedEmail(email)

            // 1. Check if hardware supports Biometrics
            val isHardwareAvailable = biometricManager.isBiometricAvailable()

            // 2. Check if user already enabled it previously
            val isAlreadyEnabled = userPreferencesDataStore.isBiometricEnabled.first()

            if (isHardwareAvailable && !isAlreadyEnabled) {
                // STOP NAVIGATION: Show the Setup Screen instead
                _uiState.update { it.copy(
                    showBiometricEnrollment = true,
                    isLoading = false
                )}
            } else {
                // ALREADY SETUP: Go straight to Chat
                userPreferencesDataStore.setLoggedIn(true)
                _uiState.update { it.copy(isLoggedIn = true, isLoading = false) }
            }
        }
    }

    private fun checkBiometricButtonVisibility() {
        viewModelScope.launch {
            // We only show the button on the Login screen if:
            // 1. Hardware exists AND 2. User has already opted-in (saved in DataStore)
            val isHardwareAvailable = biometricManager.isBiometricAvailable()
            userPreferencesDataStore.isBiometricEnabled.collect { isEnabled ->
                _uiState.update { it.copy(
                    isBiometricButtonVisible = isHardwareAvailable && isEnabled
                )}
            }
        }
    }


}