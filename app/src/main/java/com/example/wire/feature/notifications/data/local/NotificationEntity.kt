package com.example.wire.feature.notifications.data.local



import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey val id: String,
    val title: String,
    val content: String,
    val type: String,
    val timestamp: Long,
    val isRead: Boolean
)