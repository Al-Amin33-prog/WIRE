package com.example.wire.feature.chat.presentation.screen.chat_list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.wire.feature.chat.presentation.component.state.ChatUiState
import com.example.wire.feature.chat.presentation.component.viewmodel.ChatViewModel


@Composable
fun ChatListScreen(
    onChatClick: (String) -> Unit,
    onNotificationClick: () -> Unit,
    onFabClick:() -> Unit,
    viewModel: ChatViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()


    ChatContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onChatClick = onChatClick,
        onNotificationClick = onNotificationClick,
       onFabClick = onFabClick,
    )
}

@Preview
@Composable
fun ChatListPreview(){
    ChatContent(
        uiState = ChatUiState(

        ),
        onEvent = {},
        onChatClick = {},
        onNotificationClick = {},
        {}
    )

}
