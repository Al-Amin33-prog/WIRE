package com.example.wire.feature.chat.domain.usecase

import com.example.wire.core.common.util.AppError
import com.example.wire.core.common.util.Resource
import com.example.wire.feature.chat.domain.model.Message
import com.example.wire.feature.chat.domain.repository.ChatRepository
import java.io.IOException
import javax.inject.Inject

class LoadChatHistoryUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(chatId: String): Resource<List<Message>> {
        return try {
            val history = repository.loadChatHistory(chatId)
            Resource.Success(history)
        } catch (e: IOException) {
            Resource.Error(AppError.Network.NoInternet)
        } catch (e: Exception) {
            Resource.Error(AppError.Network.Unknown(e.message))
        }
    }
}