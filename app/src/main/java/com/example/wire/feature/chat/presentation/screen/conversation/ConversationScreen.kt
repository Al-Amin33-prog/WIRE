package com.example.wire.feature.chat.presentation.screen.conversation



import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.wire.core.ui.theme.WireTheme
import com.example.wire.feature.chat.domain.model.Message
import com.example.wire.feature.chat.domain.model.MessageType
import com.example.wire.feature.chat.presentation.component.event.ChatUiEvent
import com.example.wire.feature.chat.presentation.component.state.ChatUiState
import com.example.wire.feature.chat.presentation.component.viewmodel.ChatViewModel

@Composable
fun ConversationScreen(
    chatId: String,
    onBackClick: () -> Unit,
    onLongClick: ()-> Unit,
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
        onBackClick = onBackClick,
        onLongClick = {

        }

    )
}

@Preview(showBackground = true, name = "NovaPay Light Mode")
@Composable
fun ConversationScreenPreviewLight() {
    WireTheme(darkTheme = false) {
        ConversationContent(
            uiState = ChatUiState(
                displayName = "Sarah K.",
                // Ensure 'me' and 'other' IDs are distinct for alignment
                messages = listOf(
         Message(
                        id = "1",
                        senderId = "other", // Incoming (Left)
                        content = "Hey! Did you get the money? 💰",
                        timestamp = System.currentTimeMillis(),
                        type = MessageType.TEXT
                    ),
                    Message(
                        id = "2",
                        senderId = "me",    // Outgoing (Right)
                        content = "Yes I did! Thanks so much 🙏",
                        timestamp = System.currentTimeMillis(),
                        type = MessageType.TEXT
                    )
                )
            ),
            onEvent = {},
            onBackClick = {},
            onLongClick = {}
        )
    }
}

