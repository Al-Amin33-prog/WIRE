package com.example.wire.feature.chat.presentation.screen.chat_list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.wire.core.ui.theme.WireTheme
import com.example.wire.feature.chat.presentation.component.event.ChatUiEvent
import com.example.wire.feature.chat.presentation.component.state.ChatUiState
import com.example.wire.feature.chat.presentation.component.viewmodel.ChatViewModel


@Composable
fun ChatListScreen(
    onChatClick: (String) -> Unit,
    onNotificationClick: () -> Unit,
    onFabClick:() -> Unit,

    viewModel: ChatViewModel = hiltViewModel(),


) {

    val uiState by viewModel.uiState.collectAsState()
    val chatItems by viewModel.chatItems.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.onEvent(ChatUiEvent.LoadRecentChats)
    }

    WireTheme {

        ChatContent(
            uiState = uiState,
            chatItems = chatItems,
            onEvent = viewModel::onEvent,
            onChatClick = onChatClick,
            onNotificationClick = onNotificationClick,
            onFabClick = onFabClick,
        )
    }


}

@Preview
@Composable
fun ChatListPreview(){
    WireTheme(
        false
    ) {
        ChatContent(
            uiState = ChatUiState(

            ),
            onEvent = {},
            onChatClick = {},
            onNotificationClick = {},
            {},
            chatItems =emptyList(),
        )
    }


}
