package com.example.wire.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.wire.core.database.dao.ChatDao
import com.example.wire.core.database.dao.MessageDao
import com.example.wire.core.database.entity.ChatEntity    // ADD THIS IMPORT
import com.example.wire.core.database.entity.MessageEntity // ADD THIS IMPORT
import com.example.wire.feature.notifications.data.local.NotificationEntity
import com.example.wire.feature.notifications.data.local.NotificationDao

@Database(
    entities = [
        NotificationEntity::class,
        ChatEntity::class,
        MessageEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class WireDatabase : RoomDatabase() {
    abstract fun notificationDao(): NotificationDao
    abstract fun chatDao(): ChatDao
    abstract fun messageDao(): MessageDao
}