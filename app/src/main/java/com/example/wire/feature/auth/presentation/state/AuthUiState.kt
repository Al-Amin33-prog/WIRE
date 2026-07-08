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
    val triggerGoogleSignIn: Boolean = false,
    val isBiometricButtonVisible: Boolean = false,
    val showBiometricEnrollment: Boolean = false, // To trigger the "Setup" screen
    val availableBiometricType: BiometricType = BiometricType.NONE,
    val selectedBiometricType: BiometricType = BiometricType.NONE,
    val isRegistrationComplete: Boolean = false
)

enum class BiometricType {
    FINGERPRINT,
    FACE_ID,
    BOTH,
    NONE
}
