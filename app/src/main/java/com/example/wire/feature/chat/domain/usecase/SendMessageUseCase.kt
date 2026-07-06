package com.example.wire.feature.chat.domain.usecase

import com.example.wire.core.common.util.AppError
import com.example.wire.core.common.util.Resource
import com.example.wire.feature.chat.domain.repository.ChatRepository
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(chatId: String, content: String): Resource<Unit> {
        if (content.isBlank()) {
            return Resource.Error(AppError.Validation("Message cannot be empty"))
        }
        return  repository.sendMessage(chatId = chatId, content = content)


    }
}