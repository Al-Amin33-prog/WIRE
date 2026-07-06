package com.example.wire.feature.auth.domain.usecase

import com.example.wire.core.common.util.AppError
import com.example.wire.core.common.util.Resource
import com.example.wire.core.domain.base.BaseUseCase
import com.example.wire.feature.auth.domain.model.AuthUser
import com.example.wire.feature.auth.domain.repository.AuthRepository
import javax.inject.Inject

class CreateAccountUseCase @Inject constructor(
    private val authRepository: AuthRepository) : BaseUseCase<CreateAccountUseCase.Params, Resource<AuthUser>>() {

    data class Params(
        val email: String,
        val password: String,
        val displayName: String,
        val phone: String
    )

    override suspend fun invoke(params: Params): Resource<AuthUser> {
        // 1. Domain Validation
        if (params.password.length < 8) {
            return Resource.Error(AppError.Validation("Password must be at least 8 characters"))
        }
        if (params.displayName.isBlank()) {
            return Resource.Error(AppError.Validation("Display name cannot be empty"))
        }
        if (params.phone.isBlank()) {
            return Resource.Error(AppError.Validation("Phone number is required"))
        }

        // 2. Delegate to Repository
        return authRepository.register(
            params.email,
            params.password,
            params.displayName,
            params.phone
        )
    }
}
