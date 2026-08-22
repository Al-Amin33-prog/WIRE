package com.example.wire.feature.chat.data.mapper

import androidx.compose.ui.graphics.Color
import com.example.wire.core.database.entity.MessageEntity
import com.example.wire.feature.chat.data.remote.dto.ChatDto
import com.example.wire.feature.chat.data.remote.dto.MessageDto
import com.example.wire.feature.chat.domain.model.Chat
import com.example.wire.feature.chat.domain.model.Message
import com.example.wire.feature.chat.domain.model.MessageStatus
import com.example.wire.feature.chat.domain.model.MessageType
import com.example.wire.feature.chat.presentation.screen.chat_list.ChatItemData

fun MessageDto.toDomain(): Message {
    return Message(
        id = this.id,
        senderId = this.senderId,
        content = this.content,
        timestamp = this.timestamp,
        type = try {
            // FIX: Use your local enum, not Firebase
            MessageType.valueOf(this.type)
        } catch (e: Exception) {
            MessageType.TEXT
        },
        status = MessageStatus.SENT,
        isRead = true,
        isEdited = false,
        isDeleted = false,
        metadata = this.metadata ?: emptyMap(),
        receiverId = this.receiverId
    )
}

fun MessageDto.toEntity(chatId: String): MessageEntity {
    return MessageEntity(
        id = this.id,
        chatId = chatId,
        senderId = this.senderId,
        content = this.content,
        timestamp = this.timestamp,
        status = MessageStatus.SENT.name,
        type = this.type,
        isRead = true, // History messages are usually already read
        isEdited = false,
        isDeleted = false
    )
}
fun ChatDto.toDomain(): Chat {
    return Chat(
        id = uid,
        displayName = displayName,
        lastMessage = lastMessage,
        timestamp = timestamp
    )

}
fun Chat.toChatItemData(): ChatItemData {
    return ChatItemData(
        id = id,
        lastMessage = lastMessage,
        name = displayName?: "Unknown",
        time = formatChatTime(timestamp),
        unreadCount = 0,
        avatarColor = Color.Gray
    )
}
fun formatChatTime(timestamp:Long){

}