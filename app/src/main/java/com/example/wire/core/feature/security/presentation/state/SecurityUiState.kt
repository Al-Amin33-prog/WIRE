package com.example.wire.core.feature.security.presentation.state

data class
SecurityUiState(
    val step: SecurityStep = SecurityStep.Loading,
    val pin: String = "",
    val hasPin: Boolean = false,
    val biometricEnabled: Boolean = false,
    val triggerBiometricPrompt: Boolean = false,
    val errorMessage: String? = null,
    val isLoading: Boolean = false,
    val requirePinSetUp: Boolean = false,
    val confirmPin: String = "",
    val isEnrollingBiometric: Boolean = false,
    val biometricLoading: Boolean = false,
    val pinError: String? = null,
    val showBiometricSheet: Boolean = false

)