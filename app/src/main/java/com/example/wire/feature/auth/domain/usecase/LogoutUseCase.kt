package com.example.wire.feature.auth.domain.usecase

import com.example.wire.core.common.util.AppError
import com.example.wire.core.common.util.Resource
import com.example.wire.core.domain.base.BaseUseCase
import com.example.wire.feature.auth.domain.repository.AuthRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val authRepository: AuthRepository
) : BaseUseCase<Unit, Resource<Unit>>() {

    override suspend fun invoke(params: Unit): Resource<Unit> {
        return try {
            authRepository.logout()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(AppError.Network.Unknown("Failed to logout securely: ${e.message}"))
        }
    }
}