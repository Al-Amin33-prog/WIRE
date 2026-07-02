package com.example.wire.core.di

import com.example.wire.core.database.dao.ChatDao
import com.example.wire.core.database.dao.MessageDao
import com.example.wire.core.domain.dispatcher.CoroutineDispatchers
import com.example.wire.core.network.notification.NotificationHandler
import com.example.wire.core.network.websocket.WebSocketManager
import com.example.wire.core.network.websocket.WebSocketManagerImpl
import com.example.wire.feature.notifications.domain.repository.NotificationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WebSocketModule {

    @Provides
    @Singleton
    fun provideWebSocketManager(
        notificationRepository: NotificationRepository,
        notificationHandler: NotificationHandler,
        messageDao: MessageDao,
        chatDao: ChatDao,
        @ApplicationScope applicationScope: CoroutineScope, // FIXED: Added @ApplicationScope and changed type to CoroutineScope
        dispatchers: CoroutineDispatchers
    ): WebSocketManager {
        return WebSocketManagerImpl(
            notificationRepository = notificationRepository,
            notificationHandler = notificationHandler,
            messageDao = messageDao,
            chatDao = chatDao,
            applicationScope = applicationScope,
            dispatchers = dispatchers,
        )
    }
}