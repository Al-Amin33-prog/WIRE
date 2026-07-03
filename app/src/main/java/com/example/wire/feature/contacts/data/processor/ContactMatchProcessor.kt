package com.example.wire.feature.contacts.data.processor

import com.example.wire.core.common.util.AvatarUtils
import com.example.wire.core.database.dao.ChatDao
import com.example.wire.core.database.entity.ChatEntity
import com.example.wire.core.network.websocket.WebSocketProcessor
import com.example.wire.feature.chat.data.remote.dto.ChatActionDto
import javax.inject.Inject


class ContactMatchProcessor @Inject constructor(
    private val chatDao: ChatDao
) : WebSocketProcessor {
    override val action = "CONTACT_MATCH"

    override suspend fun process(actionDto: ChatActionDto) {
        val matchedUser = actionDto.message ?: return
        val name = matchedUser.metadata?.get("senderName") ?: "New Contact"
        chatDao.upsertChat(
            ChatEntity(
                chatId = matchedUser.senderId,
                contactName = name,
                lastMessage = "Recently joined Wire",
                timestamp = System.currentTimeMillis(),
                avatarColor = AvatarUtils.getColorForName(name),
                isContact = true
            )
        )
    }
}