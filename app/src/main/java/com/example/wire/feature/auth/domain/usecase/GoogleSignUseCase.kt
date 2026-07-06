package com.example.wire.feature.auth.domain.usecase

import com.example.wire.core.common.util.AppError
import com.example.wire.core.common.util.Resource
import com.example.wire.core.domain.base.BaseUseCase
import com.example.wire.feature.auth.domain.model.AuthUser
import com.example.wire.feature.auth.domain.repository.AuthRepository
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

class GoogleSignInUseCase @Inject constructor(
    private val authRepository: AuthRepository
) : BaseUseCase<String, Resource<AuthUser>>() {

    override suspend fun invoke(params: String): Resource<AuthUser> {
        // 1. Validation
        if (params.isBlank()) {
            return Resource.Error(AppError.Validation("Google token cannot be empty"))
        }

        // 2. Delegate to Repository
        return authRepository.loginWithGoogle(params)
    }
}
