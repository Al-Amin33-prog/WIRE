package com.example.wire.feature.chat.presentation.component.state

import com.example.wire.feature.chat.domain.model.Message


data class ChatUiState(

    val messages: List<Message> = emptyList(),
    val displayName: String = "User",
    val messageText: String = "",

    val currentMessage: String = "",

    val isConnected: Boolean = false,

    val isLoading: Boolean = false,

    val error: String? = null,
    val isPeerTyping: Boolean = false,
    val peerLastActive:String ="",
    val isPeerOnline: Boolean = false
)