package com.example.wire.feature.chat.data.repository



import com.example.wire.core.database.dao.ChatDao
import com.example.wire.core.database.dao.MessageDao
import com.example.wire.core.network.websocket.WebSocketManager
import com.example.wire.feature.chat.data.remote.dto.ChatApiService
import com.example.wire.feature.chat.domain.model.Message
import com.example.wire.feature.chat.domain.model.MessageType
import com.example.wire.feature.chat.data.mapper.toDomain
import com.example.wire.feature.chat.data.mapper.toEntity
import com.example.wire.feature.chat.domain.model.MessageStatus
import com.example.wire.feature.chat.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val api: ChatApiService,
    private val webSocketManager: WebSocketManager,
    private val messageDao: MessageDao,
    private val chatDao: ChatDao
) : ChatRepository {

    override suspend fun connect() {
        webSocketManager.connect()
    }

    override suspend fun disconnect() {
        webSocketManager.disconnect()
    }

    override suspend fun sendMessage(
        chatId: String,
        content: String
    ) {
        val message = Message(
            id = java.util.UUID.randomUUID().toString(),
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
        chatDao.updateChatPreview(
            chatId = chatId,
            lastMessage = content,
            timestamp = System.currentTimeMillis()
        )
        try {
            webSocketManager.sendMessage(content)
        }catch (e:Exception){

        }

    }

    override fun observeMessages(chatId: String): Flow<List<Message>> {
        return messageDao.getMessagesForChat(chatId)
            .map { entities ->
                entities.map { it.toDomain() }
            }
    }

    override suspend fun loadChatHistory(
        chatId: String
    ): List<Message> {

        return api.getChatHistory(chatId)
    }


}




