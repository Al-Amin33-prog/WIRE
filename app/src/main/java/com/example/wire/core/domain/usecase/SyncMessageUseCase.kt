package com.example.wire.core.domain.usecase

import com.example.wire.core.database.dao.MessageDao
import com.example.wire.feature.chat.data.mapper.toEntity
import com.example.wire.feature.chat.data.remote.dto.ChatApiService
import javax.inject.Inject

class SyncMessageUseCase  @Inject constructor(
    private  val chatApi: ChatApiService,
    private val messageDao: MessageDao
){
    suspend operator fun invoke(chatId: String){
        val messages = chatApi.getChatHistory(chatId)
        messages.forEach {
            messageDao.insertMessage(
                it.toEntity(chatId)
            )
        }
    }
}