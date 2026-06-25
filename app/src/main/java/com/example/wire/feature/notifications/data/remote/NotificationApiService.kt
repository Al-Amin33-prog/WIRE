package com.example.wire.feature.notifications.data.remote


import com.example.wire.feature.notifications.data.remote.dto.NotificationDto
import retrofit2.http.GET

interface NotificationApiService {
    @GET("notifications/history")
    suspend fun getNotificationHistory(): List<NotificationDto>
}