package com.example.wire.feature.chat.presentation.component.state

import com.example.wire.core.network.websocket.WebSocketState
import com.example.wire.feature.chat.domain.model.Message
import com.example.wire.feature.chat.presentation.screen.chat_list.ChatItemData

data class ChatUiState(
    val chatId: String = "",
    val chatName: String = "",
    val currentUserUid: String = "",
    val messages: List<Message> = emptyList(),
    val displayName: String = "User",
    val messageText: String = "",
    val currentMessage: String = "",
    val isConnected: Boolean = false,
    val connectionState: WebSocketState = WebSocketState.Disconnected,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isPeerTyping: Boolean = false,
    val peerLastActive: String = "",
    val isPeerOnline: Boolean = false,
    val selectedMessageId: String? = null,
    val showMessageActions: Boolean = false,
    val isConnecting: Boolean = false, // To show "Reconnecting..."
    val isTyping: Boolean = false,
    val showBiometricEnrollment: Boolean = false,
    val triggerBiometricPrompt: Boolean = false,
    val errorMessage: String? = null,
    val chats: List<ChatItemData> = emptyList()
)
