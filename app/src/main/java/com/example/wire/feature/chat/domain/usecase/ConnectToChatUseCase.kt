package com.example.wire.feature.chat.domain.usecase

import com.example.wire.core.common.util.AppError
import com.example.wire.core.common.util.Resource
import com.example.wire.feature.chat.domain.repository.ChatRepository
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

class ConnectToChatUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(): Resource<Unit> {
        return try {
            repository.connect()
            Resource.Success(Unit)
        } catch (e: SocketTimeoutException) {
            Resource.Error(AppError.Network.Timeout)
        } catch (e: IOException) {
            Resource.Error(AppError.Network.NoInternet)
        } catch (e: Exception) {
            Resource.Error(AppError.Network.Unknown(e.message))
        }
    }
}