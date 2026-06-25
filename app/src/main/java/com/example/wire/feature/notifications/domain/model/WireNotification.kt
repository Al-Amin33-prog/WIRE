package com.example.wire.feature.notifications.domain.model






data class WireNotification(
    val id: String,
    val title: String,
    val content: String,
    val type: NotificationType,
    val timestamp: Long,
    val isRead: Boolean = false
)