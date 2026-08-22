package com.example.wire.feature.chat.presentation.screen.chat_list

import androidx.compose.ui.graphics.Color

data class ChatItemData (
    val id: String,
    val name: String,
    val lastMessage: String?,
    val time: String,
    val unreadCount: Int = 0,
    val avatarColor: Color
)