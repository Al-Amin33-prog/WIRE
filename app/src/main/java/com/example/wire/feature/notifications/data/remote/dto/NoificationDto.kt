package com.example.wire.feature.notifications.data.remote.dto



import kotlinx.serialization.Serializable

@Serializable
data class NotificationDto(
    val id: String,
    val title: String,
    val content: String,
    val type: String,
    val timestamp: Long,
    val isRead: Boolean
)