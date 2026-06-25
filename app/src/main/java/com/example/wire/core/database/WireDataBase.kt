package com.example.wire.core.database


import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.wire.feature.notifications.data.local.NotificationEntity
import com.example.wire.feature.notifications.data.local.NotificationDao

@Database(
    entities = [NotificationEntity::class], // Add other entities as you build them
    version = 1,
    exportSchema = false
)
abstract class WireDatabase : RoomDatabase() {
    abstract fun notificationDao(): NotificationDao
}