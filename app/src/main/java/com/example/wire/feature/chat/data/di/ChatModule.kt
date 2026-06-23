package com.example.wire.feature.chat.data.di

import com.example.wire.core.network.websocket.WebSocketManager
import com.example.wire.feature.chat.data.repository.ChatRepositoryImpl
import com.example.wire.feature.chat.data.wrapper.ChatUseCases
import com.example.wire.feature.chat.domain.repository.ChatRepository
import com.example.wire.feature.chat.domain.usecase.ConnectToChatUseCase
import com.example.wire.feature.chat.domain.usecase.DisconnectFromChatUseCase
import com.example.wire.feature.chat.domain.usecase.LoadChatHistoryUseCase
import com.example.wire.feature.chat.domain.usecase.ObserveMessagesUseCase
import com.example.wire.feature.chat.domain.usecase.SendMessageUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ChatModule {

    @Provides
    fun provideChatUseCases(
        repository: ChatRepository
    ): ChatUseCases {

        return ChatUseCases(

            connectToChat =
                ConnectToChatUseCase(repository),

            disconnectFromChat =
                DisconnectFromChatUseCase(repository),

            observeMessages =
                ObserveMessagesUseCase(repository),

            sendMessage =
                SendMessageUseCase(repository),

            loadChatHistory =
                LoadChatHistoryUseCase(repository)
        )
    }
}

@Provides
@Singleton
fun provideChatRepository(
    webSocketManager: WebSocketManager
): ChatRepository {
    return ChatRepositoryImpl(webSocketManager)
}