package com.example.wire.feature.notifications.domain.usecase

import com.example.wire.feature.notifications.domain.repository.NotificationRepository

data class NotificationUseCases(
    val getNotifications: GetNotificationsUseCase,
    val markAsRead: MarkAsReadUseCase,
    val clearAll: ClearAllUseCases

)

class GetNotificationsUseCase(private val repository: NotificationRepository) {
    operator fun invoke() = repository.getNotifications()
}

class MarkAsReadUseCase(private val repository: NotificationRepository) {
    suspend operator fun invoke(id: String) = repository.markAsRead(id)

}
class ClearAllUseCases(private val repository: NotificationRepository) {
    suspend operator fun invoke() = repository.clearAll()
}
