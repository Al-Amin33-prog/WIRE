package com.example.wire.feature.auth.domain.usecase

import com.example.wire.core.common.util.AppError
import com.example.wire.core.common.util.Resource
import com.example.wire.core.domain.base.BaseUseCase
import com.example.wire.feature.auth.domain.model.AuthUser
import com.example.wire.feature.auth.domain.repository.AuthRepository
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

class CreateAccountUseCase @Inject constructor(
    private val authRepository: AuthRepository
) : BaseUseCase<CreateAccountUseCase.Params, Resource<AuthUser>>() {

    data class Params(
        val email: String,
        val password: String,
        val displayName: String,
        val phone: String
    )

    override suspend fun invoke(params: Params): Resource<AuthUser> {
        // 1. Validation Logic
        if (params.password.length < 8) {
            return Resource.Error(AppError.Validation("Password must be at least 8 characters"))
        }
        if (params.displayName.isBlank()) {
            return Resource.Error(AppError.Validation("Display name cannot be empty"))
        }
        if (params.phone.isBlank()) {
            return Resource.Error(AppError.Validation("Phone number is required"))
        }

        // 2. Execution and Error Mapping
        return try {
            val result = authRepository.register(
                params.email, 
                params.password, 
                params.displayName, 
                params.phone
            )
            if (result.isSuccess) {
                Resource.Success(result.getOrThrow())
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
