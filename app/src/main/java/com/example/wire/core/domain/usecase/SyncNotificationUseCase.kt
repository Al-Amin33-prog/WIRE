package com.example.wire.core.domain.usecase

import com.example.wire.core.database.dao.NotificationDao
import com.example.wire.feature.notifications.data.mapper.toEntity
import com.example.wire.feature.notifications.data.remote.NotificationApiService
import javax.inject.Inject

class SyncNotificationUseCase @Inject constructor(
    private val notificationApi: NotificationApiService,
    private val notificationDao: NotificationDao
){
    suspend operator fun invoke(){
        val notifications = notificationApi.getNotificationHistory()
        notifications.forEach {
            notificationDao.insertNotification(
                it.toEntity()
            )
        }
    }
}