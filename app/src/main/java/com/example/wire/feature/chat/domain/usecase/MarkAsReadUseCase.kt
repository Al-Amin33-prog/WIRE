package com.example.wire.feature.chat.domain.usecase

import com.example.wire.core.common.util.Resource
import com.example.wire.feature.chat.domain.repository.ChatRepository
import javax.inject.Inject

class MarkChatAsReadUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(chatId: String): Resource<Unit> {
        return repository.markChatAsRead(chatId)
    }
}