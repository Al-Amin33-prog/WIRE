package com.example.wire.feature.auth.domain.usecase

import com.example.wire.core.common.util.AppError
import com.example.wire.core.common.util.Resource
import com.example.wire.core.domain.base.BaseUseCase
import com.example.wire.feature.auth.domain.repository.AuthRepository
import javax.inject.Inject

class ForgotPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository
) : BaseUseCase<String, Resource<Unit>>() {

    override suspend fun invoke(params: String): Resource<Unit> {
        // 1. Domain Validation
        if (params.isBlank() || !params.contains("@")) {
            return Resource.Error(AppError.Validation("Please enter a valid email address"))
        }

        // 2. Delegate to Repository
        return authRepository.sendPasswordResetEmail(params)
    }
}