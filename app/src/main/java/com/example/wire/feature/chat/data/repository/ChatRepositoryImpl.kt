package com.example.wire.feature.chat.data.repository

import com.example.wire.core.common.util.AppError
import com.example.wire.core.common.util.Resource
import com.example.wire.core.database.dao.ChatDao
import com.example.wire.core.database.dao.MessageDao
import com.example.wire.core.network.websocket.WebSocketManager
import com.example.wire.core.worker.WorkScheduler
import com.example.wire.feature.auth.domain.repository.AuthRepository
import com.example.wire.feature.chat.data.remote.dto.ChatApiService
import com.example.wire.feature.chat.domain.model.*
import com.example.wire.feature.chat.data.mapper.toDomain
import com.example.wire.feature.chat.data.mapper.toEntity
import com.example.wire.feature.chat.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val api: ChatApiService,
    private val webSocketManager: WebSocketManager,
    private val messageDao: MessageDao,
    private val chatDao: ChatDao,
    private val workScheduler: WorkScheduler,
    private val authRepository: AuthRepository
) : ChatRepository {

    override suspend fun connect() {
        webSocketManager.connect()
    }

    override suspend fun disconnect() {
        webSocketManager.disconnect()
    }

    override suspend fun sendMessage(chatId: String, content: String): Resource<Unit> {
        val message = Message(
            id = UUID.randomUUID().toString(),
            senderId = "me",
            content = content,
            timestamp = System.currentTimeMillis(),
            type = MessageType.TEXT,
            isRead = false,
            status = MessageStatus.SENDING,
            isEdited = false,
            isDeleted = false,
            metadata = null
        )


        messageDao.insertMessage(message.toEntity(chatId))
        chatDao.updateChatPreview(chatId, content, System.currentTimeMillis())

        return try {
            // 2. Attempt network send
            webSocketManager.sendMessage(content)
            Resource.Success(Unit)
        } catch (e: Exception) {
            // 3. FAILURE: Trigger WorkManager for background retry
            workScheduler.scheduleMessageRetry()

            // Still return success to the UI because the DB is updated (Optimistic UI)
            // But we wrap it in an error if we want the UI to show a "Retry" icon
            Resource.Error(AppError.Network.Unknown("Offline: Message queued for retry"))
        }
    }

    override fun observeMessages(chatId: String): Flow<List<Message>> {
        return messageDao.getMessagesForChat(chatId)
            .map { entities -> entities.map { it.toDomain() } }
    }

    override suspend fun loadChatHistory(chatId: String): Resource<List<Message>> {
        return try {
            val history = api.getChatHistory(chatId).map { it.toDomain() }
            Resource.Success(history)
        } catch (e: Exception) {
            Resource.Error(AppError.Network.Unknown(e.message))
        }
    }
    override suspend fun deleteMessage(chatId: String, messageId: String): Resource<Unit> {
        return try {
            // 1. Local Update (SSOT)
            messageDao.markMessageAsDeleted(messageId)

            // 2. Network Notify (Pluggable Architecture)
            // We use the 'DELETE' action string we defined in the Switchboard
            webSocketManager.sendMessage("{\"action\":\"DELETE\", \"messageId\":\"$messageId\"}")

            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(AppError.Network.Unknown(e.message))
        }
    }


    override suspend fun markChatAsRead(chatId: String): Resource<Unit> {
        return try {
            val currentUser = authRepository.getCurrentUser()
            val myId = currentUser?.uid ?: "me"

            // 1. Local SSOT Update
            messageDao.markChatAsRead(chatId, currentUserId = myId)

            // 2. Notify Backend (Read Receipt)
            // We send an action so the other person's 'ChatMessageProcessor' can update their UI
            webSocketManager.sendMessage(
                "{\"action\":\"READ\", \"message\":{\"senderId\":\"$myId\", \"metadata\":{\"chatId\":\"$chatId\"}}}"
            )

            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(AppError.Network.Unknown(e.message))
        }
    }

    override suspend fun editMessage(messageId: String, newContent: String): Resource<Unit> {
        return try {
            val timestamp = System.currentTimeMillis()

            // 1. Update Room (SSOT)
            messageDao.updateMessageContent(messageId, newContent, timestamp)

            // 2. Notify Backend via WebSocket
            webSocketManager.sendMessage(
                "{\"action\":\"EDIT\", \"message\":{\"id\":\"$messageId\", \"content\":\"$newContent\"}}"
            )

            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(AppError.Network.Unknown(e.message))
        }
    }

}