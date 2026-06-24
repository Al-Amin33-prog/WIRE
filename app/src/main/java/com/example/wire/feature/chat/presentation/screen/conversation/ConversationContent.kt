package com.example.wire.feature.chat.presentation.screen.conversation




import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wire.core.ui.theme.*
import com.example.wire.feature.chat.presentation.component.event.ChatUiEvent
import com.example.wire.feature.chat.presentation.component.state.ChatUiState
import com.example.wire.feature.chat.presentation.screen.chat_list.ChatInputBar
import com.example.wire.feature.payments.presentation.component.PaymentMessageBubble

@Composable
fun ConversationContent(
    uiState: ChatUiState,
    onEvent: (ChatUiEvent) -> Unit,
    onBackClick: () -> Unit,
    onLongClick: (String) -> Unit
) {
    Scaffold(
        containerColor = BackgroundDark,
        topBar = {
            ConversationHeader(
                userName = uiState.displayName,
                onBackClick = onBackClick,
                uiState = uiState
            )
        },
        bottomBar = {
            ChatInputBar(
                text = uiState.messageText,
                onTextChange = { onEvent(ChatUiEvent.MessageChanged(it)) },
                onSend = { onEvent(ChatUiEvent.SendMessage) }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            reverseLayout = false // Set to true if you want items to start from bottom
        ) {
            item {
                Box(Modifier.fillMaxWidth().padding(vertical = 16.dp), contentAlignment = Alignment.Center) {
                    Text("Today", color = Color.Gray, fontSize = 12.sp)
                }
            }

            items(uiState.messages) { message ->
                val isMe = message.senderId == "me" // Replace with actual logic
                MessageBubble(
                    message = message, isMe = isMe,
                    onLongClick = { onLongClick(message.id) }
                )
            }

            // DEMO: Payment Message Bubble (Matching your screenshot)
            item {
                PaymentMessageBubble(amount = "75.00", note = "For dinner last night")
            }
        }
    }
}





