package com.example.wire.feature.chat.data.processor

import com.example.wire.core.database.dao.MessageDao
import com.example.wire.core.network.websocket.WebSocketProcessor
import com.example.wire.feature.chat.data.remote.dto.ChatActionDto
import javax.inject.Inject

class DeleteMessageProcessor @Inject constructor(
    private val messageDao: MessageDao
) : WebSocketProcessor {
    override val action = "DELETE"

    override suspend fun process(actionDto: ChatActionDto) {
        // The server sends the ID of the message to be wiped
        val messageId = actionDto.message?.id ?: return
        messageDao.markMessageAsDeleted(messageId)
    }
}
