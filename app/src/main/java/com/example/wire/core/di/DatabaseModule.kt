package com.example.wire.core.di



import android.content.Context
import androidx.room.Room
import com.example.wire.core.database.WireDatabase
import com.example.wire.core.database.dao.ChatDao
import com.example.wire.core.database.dao.MessageDao
import com.example.wire.feature.notifications.data.local.NotificationDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideWireDatabase(
        @ApplicationContext context: Context
    ): WireDatabase {
        return Room.databaseBuilder(
            context,
            WireDatabase::class.java,
            "wire_db"
        ).build()
    }


    @Provides
    fun provideMessageDao(database: WireDatabase): MessageDao {
        return database.messageDao()
    }

    @Provides
    fun provideChatDao(database: WireDatabase): ChatDao {
        return database.chatDao()
    }
    @Provides
    fun provideNotificationDao(database: WireDatabase): NotificationDao {
        return database.notificationDao()
    }
}




