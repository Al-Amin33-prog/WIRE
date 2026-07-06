package com.example.wire.feature.chat.domain.usecase



import com.example.wire.core.common.util.Resource
import com.example.wire.feature.chat.domain.repository.ChatRepository
import javax.inject.Inject

class DeleteMessageUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(chatId: String, messageId: String): Resource<Unit> {
        // Business logic: You might want to check if the user is the sender here
        return repository.deleteMessage(chatId, messageId)
    }
}