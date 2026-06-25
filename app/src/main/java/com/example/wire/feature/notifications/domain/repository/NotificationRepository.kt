package com.example.wire.feature.notifications.domain.repository



import com.example.wire.feature.notifications.domain.model.WireNotification
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    fun getNotifications(): Flow<List<WireNotification>>
    suspend fun saveNotification(notification: WireNotification)
    suspend fun markAsRead(id: String)
    suspend fun clearAll()
}