package com.example.wire.feature.auth.presentation.state

data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val displayName: String = "",
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val errorMessage: String? = null,
    val isPasswordResetEmailSent: Boolean = false,
    val isBiometricAvailable: Boolean = false,
    val isBiometricEnabled: Boolean = false,
    val showBiometricPrompt: Boolean = false,
    val phone: String = "",
    val triggerGoogleSignIn: Boolean = false
)