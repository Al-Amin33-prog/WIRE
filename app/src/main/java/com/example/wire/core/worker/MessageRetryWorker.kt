package com.example.wire.core.worker



import android.content.Context

import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.wire.core.database.dao.MessageDao
import com.example.wire.feature.chat.domain.repository.ChatRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first

@HiltWorker
class MessageRetryWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val messageDao: MessageDao,
    private val chatRepository: ChatRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        // 1. Get all messages that failed or are pending from Room
        val unsentMessages = messageDao.getUnsentMessages().first()

        if (unsentMessages.isEmpty()) return Result.success()

        var hasError = false
        unsentMessages.forEach { entity ->
            try {
                // 2. Resend via Repository (which uses WebSocket or API)
                chatRepository.sendMessage(entity.chatId, entity.content)
            } catch (e: Exception) {
                hasError = true
            }
        }

        return if (hasError) Result.retry() else Result.success()
    }
}