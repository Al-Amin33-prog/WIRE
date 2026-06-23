package com.example.wire.feature.chat.presentation.component.event

sealed interface ChatUiEvent {

    data class MessageChanged(
        val message: String
    ) : ChatUiEvent

    object SendMessage : ChatUiEvent

    object Connect : ChatUiEvent

    object Disconnect : ChatUiEvent
    data class LoadHistory(val chatId: String) : ChatUiEvent
}