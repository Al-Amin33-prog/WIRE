package com.example.wire.core.data.repository

import com.example.wire.core.database.dao.ChatDao
import com.example.wire.core.database.dao.MessageDao
import com.example.wire.core.database.entity.ChatEntity
import com.example.wire.feature.chat.data.remote.dto.ChatApiService
import com.example.wire.feature.contacts.data.repository.remote.ContactApiService
import com.example.wire.feature.notifications.data.local.NotificationDao
import com.example.wire.feature.notifications.data.remote.NotificationApiService
import com.example.wire.feature.notifications.data.mapper.toEntity as toNotificationEntity
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
    private val notificationDao: NotificationDao
) : SyncRepository {

    override suspend fun syncAll() = withContext(Dispatchers.IO) {
        try {
            // 1. Sync Notifications History
            val remoteNotifications = notificationApi.getNotificationHistory()
            remoteNotifications.forEach { dto ->
                // Use the specifically aliased mapper to return NotificationEntity
                notificationDao.insertNotification(dto.toNotificationEntity())
            }

            val recentChats = chatApi.getRecentChats()
            recentChats.forEach { chatDto ->
                // FIX: Use NAMED PARAMETERS.
                // This prevents putting 'isContact' into the 'unreadCount' slot.
                chatDao.upsertChat(
                    ChatEntity(
                        chatId = chatDto.uid,
                        contactName = chatDto.displayName ?: "User",
                        lastMessage = chatDto.lastMessage ?: "Synced from cloud",
                        timestamp = chatDto.timestamp,
                        unreadCount = 0,
                        avatarColor = -12345,
                        isContact = true
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}