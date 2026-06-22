package com.example.wire.feature.auth.domain.usecase

import com.example.wire.core.domain.base.BaseUseCase
import com.example.wire.feature.auth.domain.repository.AuthRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val authRepository: AuthRepository
) : BaseUseCase<Unit, Unit>() {

    override suspend fun invoke(params: Unit) {
        authRepository.logout()
    }
}