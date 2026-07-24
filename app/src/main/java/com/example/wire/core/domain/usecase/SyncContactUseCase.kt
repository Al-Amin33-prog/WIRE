package com.example.wire.core.domain.usecase

import com.example.wire.core.database.dao.ChatDao
import com.example.wire.core.database.entity.ChatEntity
import com.example.wire.feature.contacts.data.repository.remote.ContactApiService
import javax.inject.Inject

class SyncContactUseCase  @Inject constructor(
    private val contactApi: ContactApiService,
    private val chatDao: ChatDao
){
    suspend operator fun invoke(){
        val contacts = contactApi.getMatchedContacts()
        contacts.forEach { contact ->
            chatDao.upsertChat(
                ChatEntity(
                    chatId = contact.uid,
                    contactName = contact.displayName ?: "Wire User",
                    lastMessage = "Start a conversation",
                    timestamp = System.currentTimeMillis(),
                    unreadCount = 0,
                    avatarColor = -1,
                    isContact = true                )
            )
        }
    }
}