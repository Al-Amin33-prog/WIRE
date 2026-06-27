package com.example.wire.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey val id: String,
    val chatId: String, // This is the "Foreign Key" that links messages to a Chat
    val senderId: String,
    val content: String,
    val timestamp: Long,
    val type: String,   // Store Enum as String
    val status: String, // SENDING, SENT, ERROR
    val isRead: Boolean,
    val isEdited: Boolean,
    val isDeleted: Boolean
)