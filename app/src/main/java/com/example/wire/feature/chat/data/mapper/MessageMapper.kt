package com.example.wire.feature.chat.data.mapper

import com.example.wire.feature.chat.domain.model.Message
import com.example.wire.core.database.entity.MessageEntity // Add this

// Convert Room Entity to Domain Model (For UI)
fun MessageEntity.toDomain(): Message {
    return Message(
        id = this.id,
        senderId = this.senderId,
        content = this.content,
        timestamp = this.timestamp,
        type = com.example.wire.feature.chat.domain.model.MessageType.valueOf(this.type),
        status = com.example.wire.feature.chat.domain.model.MessageStatus.valueOf(this.status),
        isRead = this.isRead,
        isEdited = this.isEdited,
        isDeleted = this.isDeleted
    )
}

// Convert Domain Model to Room Entity (For Saving)
fun Message.toEntity(chatId: String): MessageEntity {
    return MessageEntity(
        id = this.id,
        chatId = chatId,
        senderId = this.senderId,
        content = this.content,
        timestamp = this.timestamp,
        type = this.type.name,
        status = this.status.name,
        isRead = this.isRead,
        isEdited = this.isEdited,
        isDeleted = this.isDeleted
    )
}