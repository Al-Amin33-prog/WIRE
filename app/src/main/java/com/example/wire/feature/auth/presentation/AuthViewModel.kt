package com.example.wire.feature.auth.presentation

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wire.core.ui.util.WireBiometricManager
import com.example.wire.feature.auth.domain.usecase.*
import com.example.wire.feature.auth.presentation.event.AuthUiEvent
import com.example.wire.feature.auth.presentation.state.AuthUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val createAccountUseCase: CreateAccountUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val observeAuthStateUseCase: ObserveAuthStateUseCase,
    private val forgotPasswordUseCase: ForgotPasswordUseCase,
    private val biometricManager: WireBiometricManager // <-- Injected!
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        observeAuthState()
        checkBiometricAvailability()
    }

    private fun checkBiometricAvailability() {
        _uiState.update { it.copy(isBiometricAvailable = biometricManager.isBiometricAvailable()) }
    }

    fun onEvent(event: AuthUiEvent) {
        when (event) {
            is AuthUiEvent.EmailChanged -> _uiState.update { it.copy(email = event.value) }
            is AuthUiEvent.PasswordChanged -> _uiState.update { it.copy(password = event.value) }
            AuthUiEvent.LoginClicked -> login()
            AuthUiEvent.BiometricLoginClicked -> _uiState.update { it.copy(showBiometricPrompt = true) }
            is AuthUiEvent.BiometricAuthFailed -> _uiState.update { 
                it.copy(showBiometricPrompt = false, errorMessage = event.reason) 
            }
            AuthUiEvent.BiometricAuthSucceeded -> {
                _uiState.update { it.copy(showBiometricPrompt = false) }
                // Handle success (e.g., fetch saved credentials and login)
            }
            // ... other events
            else -> { /* Handle others */ }
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

    private fun login() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = loginUseCase(LoginUseCase.Params(_uiState.value.email, _uiState.value.password))
            result.fold(
                onSuccess = { _uiState.update { it.copy(isLoading = false, isLoggedIn = true) } },
                onFailure = { error -> _uiState.update { it.copy(isLoading = false, errorMessage = error.message) } }
            )
        }
    }

    private fun observeAuthState() {
        viewModelScope.launch {
            observeAuthStateUseCase(Unit).collect { user ->
                _uiState.update { it.copy(isLoggedIn = user != null) }
            }
        }
    }
}
