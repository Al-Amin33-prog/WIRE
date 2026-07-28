package com.example.wire.core.feature.security.presentation.effect

sealed interface SecurityUiEventEffect {
    data object LaunchedBiometricPrompt: SecurityUiEventEffect
    data object NavigateToMainShell: SecurityUiEventEffect
    data object  VerificationSucceeded: SecurityUiEventEffect

}