package com.example.wire.feature.chat.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ChatDto(
    val uid: String,
    val displayName: String?,
    val lastMessage: String?,
    val timestamp: Long
)