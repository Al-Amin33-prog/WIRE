package com.example.wire.feature.chat.domain.usecase

import com.example.wire.feature.chat.domain.repository.ChatRepository
import javax.inject.Inject

class ConnectToChatUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    suspend operator fun invoke() {
        repository.connect()
    }
}