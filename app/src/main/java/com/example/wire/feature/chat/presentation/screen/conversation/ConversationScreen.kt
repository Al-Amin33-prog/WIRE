package com.example.wire.feature.chat.presentation.screen.conversation



import androidx.compose.runtime.Composable

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.wire.feature.chat.presentation.component.event.ChatUiEvent
import com.example.wire.feature.chat.presentation.component.viewmodel.ChatViewModel

@Composable
fun ConversationScreen(
    chatId: String,
    onBackClick: () -> Unit,
    viewModel: ChatViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // When the screen opens, load history and connect
    LaunchedEffect(chatId) {
        viewModel.onEvent(ChatUiEvent.LoadHistory(chatId))
        viewModel.onEvent(ChatUiEvent.Connect) // Triggers ConnectToChat
    }

    // This is where you call your existing ConversationContent
    ConversationContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onBackClick = onBackClick
    )
}