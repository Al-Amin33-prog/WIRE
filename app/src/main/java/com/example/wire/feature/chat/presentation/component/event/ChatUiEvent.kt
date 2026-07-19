package com.example.wire.feature.chat.presentation.component.event

sealed interface ChatUiEvent {

    data class MessageChanged(
        val message: String
    ) : ChatUiEvent

    object SendMessage : ChatUiEvent

    object Connect : ChatUiEvent

    object Disconnect : ChatUiEvent
    data class LoadHistory(val chatId: String) : ChatUiEvent
    object Refresh : ChatUiEvent
    data class MessageLongClick(val messageId: String) : ChatUiEvent
    object DismissMessageActions : ChatUiEvent
    data class DeleteMessage(val messageId: String) : ChatUiEvent

    data object DismissBiometricEnrollment: ChatUiEvent
    data object EnableBiometricClicked:ChatUiEvent
    data object BiometricAuthenticationSucceeded:ChatUiEvent
    data class BiometricAuthenticationFailed(val reason: String):ChatUiEvent
    data object BiometricPromptShown:ChatUiEvent



}