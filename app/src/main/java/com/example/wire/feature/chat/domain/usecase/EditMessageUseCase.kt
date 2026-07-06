package com.example.wire.feature.chat.domain.usecase

import com.example.wire.core.common.util.AppError
import com.example.wire.core.common.util.Resource
import com.example.wire.feature.chat.domain.repository.ChatRepository
import javax.inject.Inject

class EditMessageUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(messageId: String, newContent: String): Resource<Unit> {
        if (newContent.isBlank()) return Resource.Error(AppError.Validation("Content empty"))
        return repository.editMessage(messageId, newContent)
    }
}