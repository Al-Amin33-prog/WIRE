package com.example.wire.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.wire.core.database.dao.ChatDao
import com.example.wire.core.database.dao.MessageDao
import com.example.wire.core.database.dao.NotificationDao
import com.example.wire.core.database.dao.TransactionDao
import com.example.wire.core.database.entity.ChatEntity    // ADD THIS IMPORT
import com.example.wire.core.database.entity.MessageEntity // ADD THIS IMPORT
import com.example.wire.core.database.entity.TransactionEntity
import com.example.wire.core.database.entity.NotificationEntity


@Database(
    entities = [
        NotificationEntity::class,
        ChatEntity::class,
        MessageEntity::class,
        TransactionEntity::class

    ],
    version = 1,
    exportSchema = false
)
abstract class WireDatabase : RoomDatabase() {
    abstract fun notificationDao(): NotificationDao
    abstract fun chatDao(): ChatDao
    abstract fun messageDao(): MessageDao

    abstract fun transactionDao(): TransactionDao
}