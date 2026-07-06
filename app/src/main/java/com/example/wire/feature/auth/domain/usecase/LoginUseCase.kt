package com.example.wire.feature.auth.domain.usecase

import com.example.wire.core.common.util.AppError
import com.example.wire.core.common.util.Resource
import com.example.wire.core.domain.base.BaseUseCase
import com.example.wire.feature.auth.domain.model.AuthUser
import com.example.wire.feature.auth.domain.repository.AuthRepository
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) : BaseUseCase<LoginUseCase.Params, Resource<AuthUser>>() {

    data class Params(val email: String, val password: String)

    override suspend fun invoke(params: Params): Resource<AuthUser> {
        // 1. Domain Validation
        if (params.email.isBlank() || params.password.isBlank()) {
            return Resource.Error(AppError.Validation("Email and password cannot be empty"))
        }

        // 2. Delegate to Repository (It already returns Resource and handles try-catch)
        return authRepository.login(params.email, params.password)
    }
}
