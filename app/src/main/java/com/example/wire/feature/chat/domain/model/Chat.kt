package com.example.wire.feature.chat.domain.model

data class Chat(
    val id: String,
    val displayName: String?,
    val lastMessage: String?,
    val timestamp: Long
)