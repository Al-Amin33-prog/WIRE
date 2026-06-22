package com.example.wire.feature.auth.domain.usecase

import com.example.wire.core.domain.base.BaseUseCase
import com.example.wire.feature.auth.domain.repository.AuthRepository
import javax.inject.Inject

class ForgotPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository
) : BaseUseCase<String, Result<Unit>>() {

    override suspend fun invoke(params: String): Result<Unit> {
        if (params.isBlank() || !params.contains("@")) {
            return Result.failure(IllegalArgumentException("Please enter a valid email"))
        }
        return authRepository.sendPasswordResetEmail(params)
    }
}