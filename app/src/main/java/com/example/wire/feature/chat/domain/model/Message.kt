package com.example.wire.feature.chat.domain.model



import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val id: String,
    val senderId: String,
    val content: String,
    val timestamp: Long,
    val type: MessageType ,
    val isRead: Boolean = false,
    val status: MessageStatus = MessageStatus.SENT,
    val isEdited: Boolean = false,
    val isDeleted: Boolean = false,
    val metadata: Map<String, String>? = null

)

enum class MessageType {
    TEXT, IMAGE, VIDEO, FILE
}
enum class MessageStatus {
    SENDING, SENT, DELIVERED, READ, ERROR
}