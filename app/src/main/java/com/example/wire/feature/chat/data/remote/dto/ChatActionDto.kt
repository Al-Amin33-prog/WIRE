package com.example.wire.feature.chat.data.remote.dto

import com.example.wire.feature.chat.domain.model.Message
import kotlinx.serialization.Serializable

@Serializable
data class ChatActionDto(
    val action: String, // "SEND", "EDIT", "DELETE", "PAYMENT", "TYPING_ON", "TYPING_OFF"
    val message: Message? = null, // Ensure this is YOUR domain Message, not DataStore Message
    val messageId: String? = null
)