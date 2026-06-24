package com.example.wire.feature.chat.data.mapper



import com.example.wire.feature.chat.domain.model.Message
import com.example.wire.feature.chat.domain.model.MessageType
import com.example.wire.feature.chat.domain.model.MessageStatus

// Assuming your DTO has similar fields, we create an extension function
// If your DTO message is already the domain Message, you can remove .toDomain()
// in the repository. But usually, we map like this:

fun Message.toDomain(): Message {
    return Message(
        id = this.id,
        senderId = this.senderId,
        content = this.content,
        timestamp = this.timestamp,
        type = this.type,
        status = this.status,
        isEdited = this.isEdited,
        isDeleted = this.isDeleted
    )
}
