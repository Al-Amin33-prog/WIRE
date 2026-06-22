package com.example.wire.feature.auth.domain.usecase

import com.example.wire.core.domain.base.BaseUseCase
import com.example.wire.feature.auth.domain.model.AuthUser
import com.example.wire.feature.auth.domain.repository.AuthRepository
import javax.inject.Inject

class CreateAccountUseCase @Inject constructor(
    private val authRepository: AuthRepository
) : BaseUseCase<CreateAccountUseCase.Params, Result<AuthUser>>() {

    data class Params(
        val email: String,
        val password: String,
        val displayName: String,
        val phone: String
    )

    override suspend fun invoke(params: Params): Result<AuthUser> {
        if (params.password.length < 8) {
            return Result.failure(IllegalArgumentException("Password must be at least 8 characters"))
        }
        if (params.displayName.isBlank()) {
            return Result.failure(IllegalArgumentException("Display name cannot be empty"))
        }
        return authRepository.register(params.email, params.password, params.displayName,params.phone)

    }
}