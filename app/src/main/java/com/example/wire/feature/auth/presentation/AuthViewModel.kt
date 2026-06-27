package com.example.wire.feature.auth.presentation

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wire.core.datastore.preferences.UserPreferencesDataStore
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
    private val biometricManager: WireBiometricManager,
    private val googleSignInUseCase: GoogleSignInUseCase,
    private val userPreferencesDataStore: UserPreferencesDataStore
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
                // Here you would typically trigger a silent login with saved credentials
            }
            is AuthUiEvent.BiometricAuthFailed -> {
                _uiState.update { it.copy(showBiometricPrompt = false, errorMessage = event.reason) }
            }

            AuthUiEvent.GoogleSignInClicked -> _uiState.update { it.copy(triggerGoogleSignIn = true) }
            is AuthUiEvent.GoogleSignInResult -> handleGoogleSignIn(event.idToken)
            is AuthUiEvent.GoogleSignInFailed -> _uiState.update { 
                it.copy(errorMessage = event.reason, triggerGoogleSignIn = false) 
            }

        }
    }

    private fun handleGoogleSignIn(idToken: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, triggerGoogleSignIn = false) }
            val result = googleSignInUseCase(idToken)
            result.fold(
                onSuccess = {
                    userPreferencesDataStore.setLoggedIn(true)
                    _uiState.update { it.copy(isLoading = false, isLoggedIn = true) }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = error.message) }
                }
            )
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
                onSuccess = {
                    userPreferencesDataStore.setLoggedIn(true)
                    _uiState.update { it.copy(isLoading = false, isLoggedIn = true) }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = error.message) }
                }
            )
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
            val result = createAccountUseCase(
                CreateAccountUseCase.Params(
                    current.email, current.password, current.displayName, current.phone
                )
            )
            result.fold(
                onSuccess = {
                    userPreferencesDataStore.setLoggedIn(true)
                    _uiState.update { it.copy(isLoading = false, isLoggedIn = true) }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = error.message) }
                }
            )
        }
    }

    private fun logout() {
        viewModelScope.launch {
            logoutUseCase(Unit)
            userPreferencesDataStore.clearAll()
            _uiState.update { AuthUiState() }
        }
    }

    private fun sendPasswordReset() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = forgotPasswordUseCase(_uiState.value.email)
            result.fold(
                onSuccess = { _uiState.update { it.copy(isLoading = false, isPasswordResetEmailSent = true) } },
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
