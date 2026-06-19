package com.example.wire.feature.auth.domain.usecase

import com.example.wire.core.domain.base.FlowUseCase
import com.example.wire.feature.auth.domain.model.AuthUser
import com.example.wire.feature.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class ObserveAuthStateUseCase(
    private val authRepository: AuthRepository
) : FlowUseCase<Unit, AuthUser?>() {

    override fun invoke(params: Unit): Flow<AuthUser?> {
        return authRepository.observeAuthState()
    }
}