package com.example.wire.feature.notifications.domain.repository

import com.example.wire.core.database.dao.NotificationDao
import com.example.wire.core.database.entity.NotificationEntity
import com.example.wire.feature.notifications.data.remote.NotificationApiService
import com.example.wire.feature.notifications.domain.model.NotificationType
import com.example.wire.feature.notifications.domain.model.WireNotification
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val api: NotificationApiService,
    private val dao: NotificationDao
) : NotificationRepository {

    // 1. THE SSOT: Observe local database only.
    // The UI will react automatically when the API updates the DB.
    override fun getNotifications(): Flow<List<WireNotification>> {
        return dao.getAllNotifications().map { entities ->
            entities.map { entity ->
                WireNotification(
                    id = entity.id,
                    title = entity.title,
                    content = entity.content,
                    type = try {
                        NotificationType.valueOf(entity.type)
                    } catch (e: Exception) {
                        NotificationType.SYSTEM_ALERT
                    },
                    timestamp = entity.timestamp,
                    isRead = entity.isRead
                )
            }
        }
    }

    // 2. USING THE API: Fetch from server and update local storage
    override suspend fun syncNotifications() {
        try {
            val remoteNotifications = api.getNotificationHistory()
            remoteNotifications.forEach { dto ->
                // Map DTO to Entity and insert into Room
                val entity = NotificationEntity(
                    id = dto.id,
                    title = dto.title,
                    content = dto.content,
                    type = dto.type,
                    timestamp = dto.timestamp,
                    isRead = false // New syncs are unread by default
                )
                dao.insertNotification(entity)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun saveNotification(notification: WireNotification) {
        val entity = NotificationEntity(
            id = notification.id,
            title = notification.title,
            content = notification.content,
            type = notification.type.name,
            timestamp = notification.timestamp,
            isRead = notification.isRead
        )
        dao.insertNotification(entity)
    }

    override suspend fun markAsRead(id: String) {
        dao.markAsRead(id)
        // OPTIONAL: Tell the API Sarah read the notification
        // try { api.markAsRead(id) } catch(e: Exception) {}
    }

    override suspend fun clearAll() {
        dao.clearAll()
    }
}