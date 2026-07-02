package com.example.wire.feature.notifications.domain.usecase

import com.example.wire.core.common.util.AppError
import com.example.wire.core.common.util.Resource
import com.example.wire.feature.notifications.domain.model.WireNotification
import com.example.wire.feature.notifications.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

data class NotificationUseCases(
    val getNotifications: GetNotificationsUseCase,
    val markAsRead: MarkAsReadUseCase,
    val clearAll: ClearAllUseCase // Renamed to singular for consistency
)

class GetNotificationsUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    // Flows usually handle errors via .catch in the ViewModel,
    // but we can map them here to your AppError system
    operator fun invoke(): Flow<Resource<List<WireNotification>>> {
        return repository.getNotifications()
            .map { Resource.Success(it) as Resource<List<WireNotification>> }
            .catch { e ->
                if (e is IOException) emit(Resource.Error(AppError.Network.NoInternet))
                else emit(Resource.Error(AppError.Network.Unknown(e.message)))
            }
    }
}

class MarkAsReadUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(id: String): Resource<Unit> {
        return try {
            repository.markAsRead(id)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(AppError.Network.Unknown(e.message))
        }
    }
}

class ClearAllUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(): Resource<Unit> {
        return try {
            repository.clearAll()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(AppError.Network.Unknown(e.message))
        }
    }
}