package com.example.wire.feature.chat.domain.repository

import com.example.wire.core.common.util.Resource
import com.example.wire.core.network.websocket.WebSocketState
import com.example.wire.feature.chat.domain.model.Chat
import com.example.wire.feature.chat.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun getRecentChats(
        userId: String
    ):Resource<List<Chat>>
    suspend fun connect()
    suspend fun disconnect()
    
    fun observeMessages(chatId: String): Flow<List<Message>>
    
    suspend fun sendMessage(chatId: String, content: String): Resource<Unit>
    
    suspend fun loadChatHistory(senderId: String, receiverId: String): Resource<List<Message>>
    
    suspend fun deleteMessage(chatId: String, messageId: String): Resource<Unit>
    
    suspend fun markChatAsRead(chatId: String): Resource<Unit>
    
    suspend fun editMessage(messageId: String, newContent: String): Resource<Unit>
    
    fun observeConnectionState(): Flow<WebSocketState>
    fun observeTypingState(): Flow<Boolean>
}
