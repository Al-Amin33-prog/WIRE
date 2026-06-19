package com.example.wire.feature.auth.presentation.event

sealed class AuthUiEvent {
    data class EmailChanged(val value: String) : AuthUiEvent()
    data class PasswordChanged(val value: String) : AuthUiEvent()
    data class ConfirmPasswordChanged(val value: String) : AuthUiEvent()
    data class DisplayNameChanged(val value: String) : AuthUiEvent()

    object LoginClicked : AuthUiEvent()
    object CreateAccountClicked : AuthUiEvent()
    object LogoutClicked : AuthUiEvent()
    object ForgotPasswordClicked : AuthUiEvent()

    object ErrorDismissed : AuthUiEvent()
}