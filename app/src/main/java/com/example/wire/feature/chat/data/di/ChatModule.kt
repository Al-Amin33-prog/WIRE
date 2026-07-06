package com.example.wire.feature.chat.data.di

import com.example.wire.core.database.dao.ChatDao
import com.example.wire.core.database.dao.MessageDao
import com.example.wire.core.network.notification.NotificationHandler
import com.example.wire.core.network.websocket.WebSocketManager
import com.example.wire.core.network.websocket.WebSocketProcessor
import com.example.wire.core.worker.WorkScheduler
import com.example.wire.feature.chat.data.processor.ChatMessageProcessor
import com.example.wire.feature.chat.data.remote.dto.ChatApiService
import com.example.wire.feature.chat.data.repository.ChatRepositoryImpl
import com.example.wire.feature.chat.data.wrapper.ChatUseCases
import com.example.wire.feature.chat.domain.repository.ChatRepository
import com.example.wire.feature.chat.domain.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ChatModule {

    @Provides
    @Singleton
    fun provideChatApiService(retrofit: Retrofit): ChatApiService {
        return retrofit.create(ChatApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideChatRepository(
        api: ChatApiService,
        messageDao: MessageDao,
        chatDao: ChatDao,
        webSocketManager: WebSocketManager,
        workScheduler: WorkScheduler
    ): ChatRepository {
        return ChatRepositoryImpl(
            api,
            webSocketManager,
            messageDao,
            chatDao,
            workScheduler
            )
    }

    @Provides
    @Singleton
    fun provideChatUseCases(repository: ChatRepository): ChatUseCases {
        return ChatUseCases(
            connectToChat = ConnectToChatUseCase(repository),
            disconnectFromChat = DisconnectFromChatUseCase(repository),
            observeMessages = ObserveMessagesUseCase(repository),
            sendMessage = SendMessageUseCase(repository),
            loadChatHistory = LoadChatHistoryUseCase(repository),
            deleteMessage = DeleteMessageUseCase(repository)
        )
    }
    @Provides
    @IntoSet
    fun provideChatMessageProcessor(
        messageDao: MessageDao,
        chatDao: ChatDao,
        notificationHandler: NotificationHandler
    ): WebSocketProcessor {
        return ChatMessageProcessor(messageDao, chatDao, notificationHandler)
    }
}
