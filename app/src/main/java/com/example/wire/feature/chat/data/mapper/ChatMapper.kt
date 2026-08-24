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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun MessageDto.toDomain(): Message {
    return Message(
        id = this.id,
        senderId = this.senderId,
        content = this.content,
        timestamp = this.timestamp,
        type = try {
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
        isRead = true,
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
        name = displayName ?: "Unknown",
        time = formatChatTime(timestamp), // Now returns String
        unreadCount = 0,
        avatarColor = Color.Gray
    )
}

fun formatChatTime(timestamp: Long): String { // FIXED: Added return type String
    val now = Calendar.getInstance()
    val msgTime = Calendar.getInstance().apply { timeInMillis = timestamp }

    return when {
        // Today
        now.get(Calendar.YEAR) == msgTime.get(Calendar.YEAR) &&
                now.get(Calendar.DAY_OF_YEAR) == msgTime.get(Calendar.DAY_OF_YEAR) -> {
            SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(timestamp))
        }
        // Yesterday
        now.get(Calendar.YEAR) == msgTime.get(Calendar.YEAR) &&
                now.get(Calendar.DAY_OF_YEAR) - msgTime.get(Calendar.DAY_OF_YEAR) == 1 -> {
            "Yesterday"
        }
        // This week
        now.get(Calendar.WEEK_OF_YEAR) == msgTime.get(Calendar.WEEK_OF_YEAR) -> {
            SimpleDateFormat("EEE", Locale.getDefault()).format(Date(timestamp)) // Mon, Tue
        }
        // Older
        else -> {
            SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(Date(timestamp))
        }
    }
}