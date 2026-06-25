package com.example.wire.feature.notifications.data.mapper



import com.example.wire.feature.notifications.data.remote.dto.NotificationDto
import com.example.wire.feature.notifications.domain.model.WireNotification
import com.example.wire.feature.notifications.domain.model.NotificationType

fun NotificationDto.toDomain(): WireNotification {
    return WireNotification(
        id = id,
        title = title,
        content = content,
        type = try { NotificationType.valueOf(type) } catch(e: Exception) { NotificationType.SYSTEM_ALERT },
        timestamp = timestamp,
        isRead = isRead
    )
}