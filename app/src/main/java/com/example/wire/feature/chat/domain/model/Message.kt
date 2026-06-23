package com.example.wire.feature.chat.domain.model



import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val id: String,
    val senderId: String,
    val content: String,
    val timestamp: Long,
    val type: MessageType = MessageType.TEXT,
    val isRead: Boolean = false
)

enum class MessageType {
    TEXT, IMAGE, VIDEO, FILE
}