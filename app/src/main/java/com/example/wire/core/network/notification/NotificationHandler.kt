package com.example.wire.core.network.notification

import com.example.wire.feature.notifications.domain.model.NotificationType


interface NotificationHandler {
    fun showSystemAlert(title: String, message: String, type: NotificationType)
}