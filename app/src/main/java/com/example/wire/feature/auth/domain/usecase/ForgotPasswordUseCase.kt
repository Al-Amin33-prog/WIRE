package com.example.wire.feature.auth.domain.usecase

import com.example.wire.core.domain.base.BaseUseCase
import com.example.wire.feature.auth.domain.repository.AuthRepository

class ForgotPasswordUseCase(
    private val authRepository: AuthRepository
) : BaseUseCase<String, Result<Unit>>() {

    override suspend fun invoke(params: String): Result<Unit> {
        if (params.isBlank() || !params.contains("@")) {
            return Result.failure(IllegalArgumentException("Please enter a valid email"))
        }
        return authRepository.sendPasswordResetEmail(params)
    }
}