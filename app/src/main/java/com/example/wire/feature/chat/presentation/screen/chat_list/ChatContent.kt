package com.example.wire.feature.chat.presentation.screen.chat_list

import androidx.compose.runtime.Composable
import com.example.wire.feature.chat.presentation.component.event.ChatUiEvent
import com.example.wire.feature.chat.presentation.component.state.ChatUiState

@Composable
fun ChatContent(
    uiState: ChatUiState,
    onEvent: (ChatUiEvent) -> Unit
) {
    // Implement your UI here (LazyColumn for messages, TextField for input)
    // Use uiState.messageText for the value of your TextField
    // Use onEvent(ChatUiEvent.MessageChanged(it)) for onValueChange
    // Use onEvent(ChatUiEvent.SendMessage) for the send button click
}