package com.example.wire.feature.chat.data.mapper

import com.example.wire.core.database.entity.MessageEntity
import com.example.wire.feature.chat.data.remote.dto.MessageDto
import com.example.wire.feature.chat.domain.model.Message
import com.example.wire.feature.chat.domain.model.MessageStatus
import com.example.wire.feature.chat.domain.model.MessageType
import java.util.UUID

fun MessageDto.toDomain(): Message {
    return Message(
        id = this.id ?: UUID.randomUUID().toString(),
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
        // FIX: Ensure metadata is handled (assuming your domain Message supports it)
        metadata = this.metadata ?: emptyMap()
    )
}

fun MessageDto.toEntity(chatId: String): MessageEntity {
    return MessageEntity(
        id = this.id ?: UUID.randomUUID().toString(),
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