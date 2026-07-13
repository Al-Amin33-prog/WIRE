package com.example.wire.feature.chat.data.wrapper

import com.example.wire.feature.chat.domain.usecase.ConnectToChatUseCase
import com.example.wire.feature.chat.domain.usecase.DeleteMessageUseCase
import com.example.wire.feature.chat.domain.usecase.DisconnectFromChatUseCase
import com.example.wire.feature.chat.domain.usecase.EditMessageUseCase
import com.example.wire.feature.chat.domain.usecase.LoadChatHistoryUseCase
import com.example.wire.feature.chat.domain.usecase.MarkChatAsReadUseCase
import com.example.wire.feature.chat.domain.usecase.ObserveConnectionStateUseCase
import com.example.wire.feature.chat.domain.usecase.ObserveMessagesUseCase
import com.example.wire.feature.chat.domain.usecase.ObserveTypingStateUseCase
import com.example.wire.feature.chat.domain.usecase.SendMessageUseCase
import javax.inject.Inject

data class ChatUseCases @Inject constructor(
    val connectToChat: ConnectToChatUseCase,
    val disconnectFromChat: DisconnectFromChatUseCase,
    val loadChatHistory: LoadChatHistoryUseCase,
    val observeMessages: ObserveMessagesUseCase,
    val sendMessage: SendMessageUseCase,
    val deleteMessage: DeleteMessageUseCase,
    val markChatAsRead: MarkChatAsReadUseCase,
    val editMessage: EditMessageUseCase,
    val observeConnectionState: ObserveConnectionStateUseCase,
    val observeTypingState: ObserveTypingStateUseCase
)
