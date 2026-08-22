package com.example.wire.feature.chat.domain.usecase

import com.example.wire.core.common.util.Resource
import com.example.wire.feature.chat.domain.model.Chat
import com.example.wire.feature.chat.domain.repository.ChatRepository
import javax.inject.Inject

class GetRecentChatsUseCase
@Inject constructor(
    private val repository: ChatRepository
){
    suspend operator fun invoke(
        userId: String
    ): Resource<List<Chat>> {
        return repository.getRecentChats(userId)
    }
}