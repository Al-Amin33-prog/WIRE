package com.example.wire.feature.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wire.feature.auth.domain.usecase.CreateAccountUseCase
import com.example.wire.feature.auth.domain.usecase.ForgotPasswordUseCase
import com.example.wire.feature.auth.domain.usecase.LoginUseCase
import com.example.wire.feature.auth.domain.usecase.LogoutUseCase
import com.example.wire.feature.auth.domain.usecase.ObserveAuthStateUseCase
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
    private val forgotPasswordUseCase: ForgotPasswordUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        observeAuthState()
    }

    fun onEvent(event: AuthUiEvent) {
        when (event) {
            is AuthUiEvent.EmailChanged -> _uiState.update { it.copy(email = event.value) }
            is AuthUiEvent.PasswordChanged -> _uiState.update { it.copy(password = event.value) }
            is AuthUiEvent.ConfirmPasswordChanged -> _uiState.update { it.copy(confirmPassword = event.value) }
            is AuthUiEvent.DisplayNameChanged -> _uiState.update { it.copy(displayName = event.value) }

            AuthUiEvent.LoginClicked -> login()
            AuthUiEvent.CreateAccountClicked -> createAccount()
            AuthUiEvent.LogoutClicked -> logout()
            AuthUiEvent.ForgotPasswordClicked -> sendPasswordReset()

            AuthUiEvent.ErrorDismissed -> _uiState.update { it.copy(errorMessage = null) }
        }
    }

    private fun login() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val result = loginUseCase(
                LoginUseCase.Params(
                    email = _uiState.value.email,
                    password = _uiState.value.password
                )
            )

            result.fold(
                onSuccess = { authUser ->
                    syncUserWithBackend(authUser.token)
                    _uiState.update {
                        it.copy(isLoading = false, isLoggedIn = true)

                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = error.message ?: "Login failed")
                    }
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
                    email = current.email,
                    password = current.password,
                    displayName = current.displayName
                )
            )

            result.fold(
                onSuccess = { authUser ->
                    syncUserWithBackend(authUser.token)
                    _uiState.update { it.copy(isLoading = false, isLoggedIn = true) }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false, errorMessage = error.message ?:
                            "Registration failed"
                        )
                    }
                }
            )
        }
    }

    private fun logout() {
        viewModelScope.launch {
            logoutUseCase(Unit)
            _uiState.update { AuthUiState() } // reset to clean state
        }
    }

    private fun sendPasswordReset() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val result = forgotPasswordUseCase(_uiState.value.email)

            result.fold(
                onSuccess = {
                    _uiState.update { it.copy(
                        isLoading = false, isPasswordResetEmailSent = true
                    ) }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false, errorMessage = error.message ?:
                            "Failed to send reset email"
                        )
                    }
                }
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
    private fun syncUserWithBackend(token: String) {
        viewModelScope.launch {
            // This is where your network layer passes the token string
            // to the IntelliJ Ktor backend via your API repository wrapper.
            // Once HTTP request finishes:
            _uiState.update { it.copy(isLoading = false, isLoggedIn = true) }
        }
    }
}