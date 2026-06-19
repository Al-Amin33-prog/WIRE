package com.example.wire.feature.auth.domain.usecase

import com.example.wire.core.domain.base.BaseUseCase
import com.example.wire.feature.auth.domain.model.AuthUser
import com.example.wire.feature.auth.domain.repository.AuthRepository

class LoginUseCase(
    private val authRepository: AuthRepository
) : BaseUseCase<LoginUseCase.Params, Result<AuthUser>>() {

    data class Params(
        val email: String,
        val password: String
    )

    override suspend fun invoke(params: Params): Result<AuthUser> {
        if (params.email.isBlank() || params.password.isBlank()) {
            return Result.failure(
                IllegalArgumentException(
                    "Email and password cannot be empty"))
        }
        return authRepository.login(params.email, params.password)
    }
}