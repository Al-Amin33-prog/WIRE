package com.example.wire.core.domain.usecase

import com.example.wire.core.database.dao.ChatDao
import com.example.wire.core.database.entity.ChatEntity
import com.example.wire.feature.chat.data.remote.dto.ChatApiService
import javax.inject.Inject

class SyncChatsUseCase @Inject constructor(
    private val chatApi: ChatApiService,
    private val chatDao: ChatDao,
    private val syncMessages: SyncMessageUseCase
){
    suspend operator fun invoke(){
        val chats = chatApi.getRecentChats()
        chats.forEach { chat ->
            chatDao.upsertChat(
                ChatEntity(
                    chatId = chat.uid,
                    contactName = chat.displayName ?: "User",
                    lastMessage = chat.lastMessage ?: "",
                    timestamp = chat.timestamp,
                    unreadCount = 0,
                    avatarColor = -1,
                    isContact = true
                )
            )
            syncMessages(chat.uid)
        }
    }
}