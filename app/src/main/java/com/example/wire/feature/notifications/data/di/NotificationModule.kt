package com.example.wire.feature.notifications.data.di

import com.example.wire.feature.notifications.domain.usecase.ClearAllUseCases
import com.example.wire.feature.notifications.domain.usecase.GetNotificationsUseCase
import com.example.wire.feature.notifications.domain.usecase.MarkAsReadUseCase
import com.example.wire.feature.notifications.domain.usecase.NotificationUseCases
import android.content.Context
import com.example.wire.feature.notifications.data.remote.NotificationApiService


import com.example.wire.core.database.WireDatabase
import com.example.wire.core.network.notification.NotificationHandler // Ensure this path matches your core
import com.example.wire.feature.notifications.data.local.NotificationDao
import com.example.wire.feature.notifications.data.local.NotificationHandlerImpl

import com.example.wire.feature.notifications.domain.repository.NotificationRepository
import com.example.wire.feature.notifications.domain.repository.NotificationRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotificationModule {

    @Provides
    @Singleton
    fun provideNotificationApiService(retrofit: Retrofit): NotificationApiService {
        return retrofit.create(NotificationApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideNotificationRepository(
        api: NotificationApiService,
        dao: NotificationDao
    ): NotificationRepository {
        return NotificationRepositoryImpl(api, dao)
    }

    @Provides
    @Singleton
    fun provideNotificationHandler(
        @ApplicationContext context: Context
    ): NotificationHandler {
        return NotificationHandlerImpl(context)
    }

    @Provides
    @Singleton
    fun provideNotificationDao(database: WireDatabase): NotificationDao {
        return database.notificationDao()
    }

    @Provides
    @Singleton
    fun provideNotificationUseCases(
        repository: NotificationRepository
    ): NotificationUseCases {
        return NotificationUseCases(
            getNotifications = GetNotificationsUseCase(repository),
            markAsRead = MarkAsReadUseCase(repository),
           clearAll = ClearAllUseCases(repository)
        )
    }
}