package com.example.wire.feature.chat.presentation.screen.conversation



import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.wire.core.ui.theme.WireTheme
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

@Preview(showBackground = true, name = "Light Mode")
@Composable
fun ConversationScreenPreviewLight() {
    WireTheme(darkTheme = false) {
        ConversationContent(
            uiState = ChatUiState(
                displayName = "Sarah K.",
                messages = listOf(
                    com.example.wire.feature.chat.domain.model.Message(
                        id = "1",
                        senderId = "other",
                        content = "Hey! Did you get the money? 💰",
                        timestamp = System.currentTimeMillis(),
                        type = MessageType.TEXT
                    ),
                    com.example.wire.feature.chat.domain.model.Message(
                        id = "2",
                        senderId = "me",
                        content = "75.00", // The logic in your bubble will detect the type
                        timestamp = System.currentTimeMillis(),
                        type = MessageType.TEXT,
                        metadata = mapOf("type" to "PAYMENT_SENT")
                    )
                )
            ),
            onEvent = {},
            onBackClick = {},
            onLongClick = {}
        )
    }
}

