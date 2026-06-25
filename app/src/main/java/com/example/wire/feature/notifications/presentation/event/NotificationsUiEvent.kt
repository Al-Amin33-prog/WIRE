package com.example.wire.feature.notifications.presentation.event



sealed class NotificationUIEvent {
    object Refresh : NotificationUIEvent()
    data class MarkAsRead(val id: String) : NotificationUIEvent()
    object ClearAll : NotificationUIEvent()
}
