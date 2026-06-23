package com.example.wire.feature.chat.presentation.screen.chat_list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel

import com.example.wire.feature.chat.presentation.component.viewmodel.ChatViewModel

@Composable
fun ChatListScreen(
    onChatClick: (String) -> Unit,
    viewModel: ChatViewModel = hiltViewModel()
) {
    // Observe the state from ViewModel
    val uiState by viewModel.uiState.collectAsState()

    // Pass state and events to the UI Content
    ChatContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onChatClick = onChatClick
    )
}

