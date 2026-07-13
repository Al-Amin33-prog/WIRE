package com.example.wire.feature.auth.domain.usecase



import com.example.wire.feature.auth.domain.model.AuthUser
import com.example.wire.feature.auth.domain.repository.AuthRepository
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    // We use the operator fun invoke to make it callable like a function
   suspend  operator fun invoke(): AuthUser? {
        return repository.getCurrentUser()
    }
}