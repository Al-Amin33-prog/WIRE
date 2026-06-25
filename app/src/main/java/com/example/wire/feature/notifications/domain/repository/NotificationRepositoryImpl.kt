package com.example.wire.feature.notifications.domain.repository



import com.example.wire.feature.notifications.data.local.NotificationDao
import com.example.wire.feature.notifications.data.local.NotificationEntity
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

    // Logic to observe Room
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

    override suspend fun saveNotification(notification: WireNotification) {

       val entity = NotificationEntity(
           id = notification.id,
           title = notification.title,
           content = notification.content,
           type = notification.type.name, // Save Enum as String
           timestamp = notification.timestamp,
           isRead = notification.isRead
       )
        dao.insertNotification(entity)
    }

    override suspend fun markAsRead(id: String) {
        dao.markAsRead(id)
    }

    override suspend fun clearAll() {
        dao.clearAll()
    }
}