package com.example.wire.feature.chat.domain.usecase


import com.example.wire.core.common.util.Resource
import com.example.wire.feature.chat.domain.model.Message
import com.example.wire.feature.chat.domain.repository.ChatRepository
import javax.inject.Inject

class LoadChatHistoryUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(
        senderId: String,
        receiverId: String
    ): Resource<List<Message>> {
        return repository.loadChatHistory(
            senderId,
            receiverId
        )
    }
}