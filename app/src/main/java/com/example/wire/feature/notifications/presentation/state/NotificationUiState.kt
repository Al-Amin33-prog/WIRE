package com.example.wire.feature.notifications.presentation.state



import com.example.wire.feature.notifications.domain.model.WireNotification

data class NotificationUIState(
    val notifications: List<WireNotification> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)