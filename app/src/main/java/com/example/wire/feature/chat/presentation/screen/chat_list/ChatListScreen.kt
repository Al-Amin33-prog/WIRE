package com.example.wire.feature.chat.presentation.screen.chat_list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.wire.feature.chat.presentation.component.viewmodel.ChatViewModel


@Composable
fun ChatListScreen(
    onChatClick: (String) -> Unit,
    onNotificationClick: () -> Unit, // Add this parameter
    viewModel: ChatViewModel = hiltViewModel()
) {
    // Observe the state from ViewModel
    val uiState by viewModel.uiState.collectAsState()

    // Pass state and events to the UI Content
    ChatContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onChatClick = onChatClick,
        onNotificationClick = onNotificationClick // Use the callback here
    )
}

@Preview
@Composable
fun ChatListPreview(){
    // Add preview implementation if needed
}
