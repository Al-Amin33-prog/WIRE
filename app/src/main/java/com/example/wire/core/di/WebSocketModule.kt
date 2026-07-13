package com.example.wire.core.di

import android.content.Context
import com.example.wire.core.database.dao.ChatDao
import com.example.wire.core.domain.dispatcher.CoroutineDispatchers
import com.example.wire.core.network.websocket.WebSocketManager
import com.example.wire.core.network.websocket.WebSocketManagerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WebSocketModule {

    @Provides
    @Singleton
    fun provideWebSocketManager(
        @ApplicationContext context: Context,
        @ApplicationScope applicationScope: CoroutineScope,
        dispatchers: CoroutineDispatchers,
        chatDao: ChatDao
    ): WebSocketManager {
        return WebSocketManagerImpl(
            applicationScope = applicationScope,
            context = context,
            dispatchers = dispatchers,
            //chatDao = chatDao
        )
    }
}



