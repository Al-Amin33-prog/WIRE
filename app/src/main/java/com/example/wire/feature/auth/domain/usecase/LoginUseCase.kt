package com.example.wire.feature.auth.domain.usecase

import com.example.wire.core.common.util.AppError
import com.example.wire.core.common.util.Resource
import com.example.wire.core.domain.base.BaseUseCase
import com.example.wire.feature.auth.domain.model.AuthUser
import com.example.wire.feature.auth.domain.repository.AuthRepository
import java.io.IOException
import java.net.SocketTimeoutException

class LoginUseCase(
    private val authRepository: AuthRepository
) : BaseUseCase<LoginUseCase.Params, Resource<AuthUser>>() {

    data class Params(
        val email: String,
        val password: String
    )

    override suspend fun invoke(params: Params): Resource<AuthUser> {
        if (params.email.isBlank() || params.password.isBlank()) {
            return Resource.Error(AppError.Validation("Email and password cannot be empty"))
        }

        return try {
            val result = authRepository.login(params.email, params.password)
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
