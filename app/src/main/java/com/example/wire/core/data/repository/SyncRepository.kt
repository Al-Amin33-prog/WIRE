package com.example.wire.core.data.repository

import com.example.wire.core.common.util.PerformanceMonitor
import com.example.wire.core.database.dao.ChatDao
import com.example.wire.core.database.dao.MessageDao
import com.example.wire.core.database.entity.ChatEntity
import com.example.wire.feature.chat.data.remote.dto.ChatApiService
import com.example.wire.feature.contacts.data.repository.remote.ContactApiService
import com.example.wire.feature.notifications.data.local.NotificationDao
import com.example.wire.feature.notifications.data.local.NotificationEntity
import com.example.wire.feature.notifications.data.remote.NotificationApiService
import com.example.wire.feature.notifications.data.mapper.toEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

interface SyncRepository {
    suspend fun syncAll()
}

@Singleton
class SyncRepositoryImpl @Inject constructor(
    private val chatApi: ChatApiService,
    private val contactApi: ContactApiService,
    private val notificationApi: NotificationApiService,
    private val messageDao: MessageDao,
    private val chatDao: ChatDao,
    private val notificationDao: NotificationDao,
    private val performanceMonitor: PerformanceMonitor
) : SyncRepository {

    override suspend fun syncAll() = withContext(Dispatchers.IO) {
        val startTime = System.currentTimeMillis()
        try {

            val duration = System.currentTimeMillis() - startTime
            performanceMonitor.recordEvent(duration)
            println("WIRE_HEALTH: P99 Sync Latency is ${performanceMonitor.getP99()} ms")
            // Sync Notifications
            val remoteNotifications = notificationApi.getNotificationHistory()
            remoteNotifications.forEach { dto ->
                val entity: NotificationEntity = dto.toEntity()
                notificationDao.insertNotification(entity)
            }

            // Sync Recent Chats
            val recentChats = chatApi.getRecentChats()
            recentChats.forEach { chatDto ->
                chatDao.upsertChat(
                    ChatEntity(
                        chatId = chatDto.uid,
                        contactName = chatDto.displayName ?: "User",
                        lastMessage = chatDto.lastMessage ?: "",
                        timestamp = chatDto.timestamp,
                        unreadCount = 0,
                        avatarColor = -1,
                        isContact = true
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
