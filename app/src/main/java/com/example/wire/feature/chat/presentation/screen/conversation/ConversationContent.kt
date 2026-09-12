package com.example.wire.feature.chat.presentation.screen.conversation





import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wire.feature.chat.presentation.component.event.ChatUiEvent
import com.example.wire.feature.chat.presentation.component.state.ChatUiState
import com.example.wire.feature.chat.presentation.screen.chat_list.ChatInputBar
import com.example.wire.feature.chat.presentation.screen.component.ConnectivityBanner
import com.example.wire.feature.payments.presentation.component.PaymentMessageBubble

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun ConversationContent(
    uiState: ChatUiState,
    onEvent: (ChatUiEvent) -> Unit,
    onBackClick: () -> Unit,
    onLongClick: (String) -> Unit,
    onNavigateToSendMoney: (recipientId: String, recipientName: String) -> Unit,
    onNavigateToRequestMoney: (recipientId: String, recipientName: String) -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
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
                onSend = { onEvent(
                    ChatUiEvent.SendMessage(chatId = uiState.chatId)
                ) },
                onSendMoneyClick = {
                    onNavigateToSendMoney(uiState.chatId, uiState.chatName)
                },
                onRequestMoneyClick = {
                    onNavigateToRequestMoney(uiState.chatId, uiState.chatName)
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {


            ConnectivityBanner(uiState = uiState)

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                reverseLayout = false
            ) {
                item {
                    Box(Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp), contentAlignment = Alignment.Center) {
                        Text("Send", color = Color.Gray, fontSize = 12.sp)
                    }
                }

                items(uiState.messages) { message ->
                    // Robust check: It's me if it matches my UID, or explicitly marked "me", 
                    // or if we are chatting with an assistant and the sender is NOT that assistant.
                    val isMe = message.senderId == uiState.currentUserUid || 
                               message.senderId == "me" || 
                               (message.senderId != uiState.chatId && uiState.chatId == "wire_assistant")

                    MessageBubble(
                        message = message,
                        isMe = isMe,
                        onLongClick = {
                            onEvent(ChatUiEvent.MessageLongClick(message.id))
                        },
                        onPayRequestClick = { amount ->
                            onNavigateToSendMoney(uiState.chatId, uiState.chatName)
                        }
                    )

                    // ... (ModalBottomSheet logic)
                }

                // TYPING INDICATOR
                if (uiState.isPeerTyping) {
                    item {
                        Text(
                            text = "${uiState.chatName} is typing...",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }


            }
        }
    }
}






