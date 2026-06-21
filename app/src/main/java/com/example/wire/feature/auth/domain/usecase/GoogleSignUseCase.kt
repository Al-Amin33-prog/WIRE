package com.example.wire.feature.auth.domain.usecase

import com.example.wire.core.domain.base.BaseUseCase
import com.example.wire.feature.auth.domain.model.AuthUser
import com.example.wire.feature.auth.domain.repository.AuthRepository

class GoogleSignInUseCase(
    private val authRepository: AuthRepository
) : BaseUseCase<String, Result<AuthUser>>() {

    override suspend fun invoke(params: String): Result<AuthUser> {
        if (params.isBlank()) {
            return Result.failure(IllegalArgumentException("Google token cannot be empty"))
        }
        return authRepository.loginWithGoogle(params)
    }
}