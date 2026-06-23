package com.example.wire.core.di

import com.example.wire.core.network.websocket.WebSocketManager
import com.example.wire.core.network.websocket.WebSocketManagerImpl
import com.google.android.datatransport.runtime.dagger.Module
import com.google.android.datatransport.runtime.dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WebSocketModule {

    @Provides
    @Singleton
    fun provideWebSocketManager(): WebSocketManager {

        return WebSocketManagerImpl()
    }
}