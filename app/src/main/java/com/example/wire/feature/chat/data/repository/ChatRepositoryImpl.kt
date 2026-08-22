package com.example.wire.feature.chat.data.repository

import com.example.wire.core.common.util.AppError
import com.example.wire.core.common.util.Resource
import com.example.wire.core.database.dao.ChatDao
import com.example.wire.core.database.dao.MessageDao
import com.example.wire.core.network.websocket.WebSocketManager
import com.example.wire.core.network.websocket.WebSocketState
import com.example.wire.core.worker.WorkScheduler
import com.example.wire.feature.auth.domain.repository.AuthRepository
import com.example.wire.feature.chat.data.mapper.toDomain
import com.example.wire.feature.chat.data.mapper.toEntity
import com.example.wire.feature.chat.data.remote.dto.ChatActionDto
import com.example.wire.feature.chat.data.remote.dto.ChatApiService
import com.example.wire.feature.chat.domain.model.Chat
import com.example.wire.feature.chat.domain.model.Message
import com.example.wire.feature.chat.domain.model.MessageStatus
import com.example.wire.feature.chat.domain.model.MessageType
import com.example.wire.feature.chat.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
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

    override fun observeMessages(chatId: String): Flow<List<Message>> {
        return messageDao.getMessagesForChat(chatId)
            .map { entities -> entities.map { it.toDomain() } }
    }

    override suspend fun sendMessage(chatId: String, content: String): Resource<Unit> {
        val currentUser = authRepository.getCurrentUser()
        val myId = currentUser?.uid ?: return Resource.Error(
            AppError.Network.Unknown("User not logged in")
        )

        val message = Message(
            id = UUID.randomUUID().toString(),
            senderId = myId,
            receiverId = chatId,
            content = content,
            timestamp = System.currentTimeMillis(),
            type = MessageType.TEXT,
            isRead = false,
            status = MessageStatus.SENDING,
            isEdited = false,
            isDeleted = false,
            metadata = null
        )

        // 1. Local Persistence (SSOT)
        messageDao.insertMessage(message.toEntity(chatId))
        chatDao.updateChatPreview(chatId, content, System.currentTimeMillis())

        return try {
            // 2. Attempt Network Send
            val action = ChatActionDto(
                action = "SEND",
                message = message
            )
            val json = Json.encodeToString(action)
            webSocketManager.sendMessage(json)
            Resource.Success(Unit)
        } catch (e: Exception) {
            // 3. Background Recovery
            workScheduler.scheduleMessageRetry()
            Resource.Error(AppError.Network.Unknown("Offline: Message queued for retry"))
        }
    }

    override suspend fun loadChatHistory(senderId: String, receiverId: String): Resource<List<Message>> {
        return try {
            val history = api.getChatHistory(senderId, receiverId).map { it.toDomain() }
            Resource.Success(history)
        } catch (e: Exception) {
            Resource.Error(AppError.Network.Unknown(e.message))
        }
    }

    override suspend fun deleteMessage(chatId: String, messageId: String): Resource<Unit> {
        return try {
            // 1. Local Delete (SSOT)
            messageDao.markMessageAsDeleted(messageId)

            // 2. Network Notify
            val action = "{\"action\":\"DELETE\", \"messageId\":\"$messageId\"}"
            webSocketManager.sendMessage(action)

            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(AppError.Network.Unknown(e.message))
        }
    }

    override suspend fun markChatAsRead(chatId: String): Resource<Unit> {
        return try {
            val currentUser = authRepository.getCurrentUser()
            val myId = currentUser?.uid ?: "me"

            // 1. Local Update
            messageDao.markChatAsRead(chatId, currentUserId = myId)

            // 2. Network Notify
            val action = "{\"action\":\"READ\", \"message\":{\"senderId\":\"$myId\", \"metadata\":{\"chatId\":\"$chatId\"}}}"
            webSocketManager.sendMessage(action)

            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(AppError.Network.Unknown(e.message))
        }
    }

    override suspend fun editMessage(messageId: String, newContent: String): Resource<Unit> {
        return try {
            val timestamp = System.currentTimeMillis()

            // 1. Local Update
            messageDao.updateMessageContent(messageId, newContent, timestamp)

            // 2. Network Notify
            val action = "{\"action\":\"EDIT\", \"message\":{\"id\":\"$messageId\", \"content\":\"$newContent\"}}"
            webSocketManager.sendMessage(action)

            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(AppError.Network.Unknown(e.message))
        }
    }

    override fun observeConnectionState(): Flow<WebSocketState> {
        return webSocketManager.connectionState()
    }

    override fun observeTypingState(): Flow<Boolean> {
        return webSocketManager.isTyping()
    }

    override suspend fun getRecentChats(userId: String): Resource<List<Chat>> {
        return try{
            val chats = api.getRecentChats(userId)
                .map { it.toDomain() }
            Resource.Success(chats)
        }catch(e:Exception){
            Resource.Error(
                AppError.Network.Unknown(e.message)
            )
        }
    }
}
