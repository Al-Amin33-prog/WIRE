package com.example.wire.feature.chat.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class MessageDto(
    val id: String,    val senderId: String,
    val content: String,
    val timestamp: Long,
    val type: String
)