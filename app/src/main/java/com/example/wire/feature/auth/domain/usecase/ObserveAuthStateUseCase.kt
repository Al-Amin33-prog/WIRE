package com.example.wire.feature.auth.domain.usecase

import com.example.wire.core.domain.base.FlowUseCase
import com.example.wire.feature.auth.domain.model.AuthUser
import com.example.wire.feature.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

class ObserveAuthStateUseCase @Inject constructor(
    private val authRepository: AuthRepository
) : FlowUseCase<Unit, AuthUser?>() {

    override fun invoke(params: Unit): Flow<AuthUser?> {
        return authRepository.observeAuthState()
            .catch { e ->
                // You can log errors here if the Auth stream fails
                emit(null)
            }
    }
}