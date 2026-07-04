package com.example.wire.feature.chat.data.mapper



import com.example.wire.core.database.entity.MessageEntity
import com.example.wire.feature.chat.data.remote.dto.MessageDto
import com.example.wire.feature.chat.domain.model.MessageStatus
import java.util.UUID

/**
 * Converts a Network Message DTO to a Database Message Entity
 * Used by SyncRepository to anchor cloud data into Room.
 */
fun MessageDto.toEntity(chatId: String): MessageEntity {
    return MessageEntity(
        id = this.id ?: UUID.randomUUID().toString(),
        chatId = chatId,
        senderId = this.senderId,
        content = this.content,
        timestamp = this.timestamp,
        // Since we are fetching this from the server,
        // we know it was successfully sent.
        status = MessageStatus.SENT.name,
        type = this.type,
        isRead = false,
        isEdited = false,
        isDeleted = false
    )
}

