package com.example.wire.core.data.repository

// 1. REMOVE: import androidx.compose.foundation.gestures.forEach
// Use standard Kotlin forEach (no import needed usually)

import com.example.wire.core.common.util.PerformanceMonitor
import com.example.wire.core.database.dao.ChatDao
import com.example.wire.core.database.dao.MessageDao
import com.example.wire.core.database.entity.ChatEntity
import com.example.wire.feature.chat.data.remote.dto.ChatApiService
import com.example.wire.feature.chat.data.mapper.toEntity
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
    private val notificationDao: NotificationDao,
    private val performanceMonitor: PerformanceMonitor
) : SyncRepository {

    override suspend fun syncAll() = withContext(Dispatchers.IO) {
        val startTime = System.currentTimeMillis()
        try {
            // 1. Sync Notifications
            val remoteNotifications = notificationApi.getNotificationHistory()
            remoteNotifications.forEach { dto ->
                notificationDao.insertNotification(dto.toNotificationEntity())
            }

            // 2. Sync Contacts
            // FIXED: Use the correct method name and handle AuthUserDto fields
            val registeredContacts = contactApi.getMatchedContacts()
            registeredContacts.forEach { contactDto ->
                chatDao.upsertChat(
                    ChatEntity(
                        chatId = contactDto.uid,
                        contactName = contactDto.displayName ?: "Wire User",
                        lastMessage = "Start a conversation", // Default for contacts
                        timestamp = System.currentTimeMillis(),
                        unreadCount = 0,
                        avatarColor = -1,
                        isContact = true
                    )
                )
            }

            // 3. Sync Recent Chats & Missed Messages
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

                // Fetch and save actual messages
                val messages = chatApi.getChatHistory(chatDto.uid)
                messages.forEach { msgDto ->
                    messageDao.insertMessage(msgDto.toEntity(chatId = chatDto.uid))
                }
            }

            // Record Performance
            val duration = System.currentTimeMillis() - startTime
            performanceMonitor.recordEvent(duration)
            println("WIRE_HEALTH: P99 Sync Latency is ${performanceMonitor.getP99()} ms")

        } catch (e: Exception) {
            performanceMonitor.recordEvent(5000)
            e.printStackTrace()
        }
    }
}