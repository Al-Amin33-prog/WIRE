package com.example.wire.feature.auth.domain.usecase

import com.example.wire.core.common.util.AppError
import com.example.wire.core.common.util.Resource
import com.example.wire.core.domain.base.BaseUseCase
import com.example.wire.feature.auth.domain.repository.AuthRepository
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

class ForgotPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository
) : BaseUseCase<String, Resource<Unit>>() {

    override suspend fun invoke(params: String): Resource<Unit> {
        // 1. Validation
        if (params.isBlank() || !params.contains("@")) {
            return Resource.Error(AppError.Validation("Please enter a valid email address"))
        }

        // 2. Execution
        return try {
            val result = authRepository.sendPasswordResetEmail(params)
            if (result.isSuccess) {
                Resource.Success(Unit)
            } else {
                Resource.Error(AppError.Network.Unknown(result.exceptionOrNull()?.message))
            }
        } catch (e: SocketTimeoutException) {
            Resource.Error(AppError.Network.Timeout)
        } catch (e: IOException) {
            Resource.Error(AppError.Network.NoInternet)
        } catch (e: Exception) {
            Resource.Error(AppError.Network.Unknown(e.message))
        }
    }
}