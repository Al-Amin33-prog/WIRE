package com.example.wire.feature.chat.domain.usecase

import com.example.wire.feature.chat.domain.repository.ChatRepository
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(
        chatId: String,
        content: String
    ) {
        repository.sendMessage(
            chatId = chatId,
            content = content
        )
    }
}