package com.example.wire.feature.notifications.data.mapper

import com.example.wire.feature.notifications.data.local.NotificationEntity
import com.example.wire.feature.notifications.data.remote.dto.NotificationDto
import com.example.wire.feature.notifications.domain.model.WireNotification
import com.example.wire.feature.notifications.domain.model.NotificationType

/**
 * Maps Network DTO to Room Entity (For Syncing/Saving to DB)
 */
fun NotificationDto.toEntity(): NotificationEntity {
    return NotificationEntity(
        id = id,
        title = title,
        content = content,
        type = type, // Room stores enum names as Strings
        timestamp = timestamp,
        isRead = false // Default to unread when synced from server
    )
}

/**
 * Maps Network DTO to Domain Model (For direct UI display if needed)
 */
fun NotificationDto.toDomain(): WireNotification {
    return WireNotification(
        id = id,
        title = title,
        content = content,
        type = try {
            NotificationType.valueOf(type)
        } catch(e: Exception) {
            NotificationType.SYSTEM_ALERT
        },
        timestamp = timestamp,
        isRead = false
    )
}