package com.example.wire.feature.chat.domain.usecase

import com.example.wire.feature.chat.domain.repository.ChatRepository
import javax.inject.Inject

class ObserveMessagesUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    operator fun invoke(chatId: String) =
        repository.observeMessages(chatId)
}