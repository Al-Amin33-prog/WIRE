package com.example.wire.core.feature.security.presentation.event

sealed interface SecurityUiEvent {
    data class PinChanged(
        val value: String
    ): SecurityUiEvent
    object   CreatePinClicked: SecurityUiEvent
     object EnableBiometricClicked: SecurityUiEvent

    data class BiometricFailed(
        val message: String
    ): SecurityUiEvent
    data object DismissEnrollment : SecurityUiEvent
    data object BiometricAuthenticationSucceeded: SecurityUiEvent
    data class ConfirmPinChanged(
        val value: String
    ): SecurityUiEvent
    data object  BiometricSheetDismissed : SecurityUiEvent
    data class VerificationPinChanged(
        val value: String
    ): SecurityUiEvent
    data object VerifyPinClicked: SecurityUiEvent
    data object  CancelVerification: SecurityUiEvent

}