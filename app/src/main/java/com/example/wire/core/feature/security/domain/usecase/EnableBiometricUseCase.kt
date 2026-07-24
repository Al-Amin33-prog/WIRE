package com.example.wire.core.feature.security.domain.usecase

import com.example.wire.core.domain.base.BaseUseCase
import com.example.wire.core.feature.security.domain.repository.SecurityRepository
import javax.inject.Inject

class EnableBiometricUseCase @Inject constructor(
    private val repository: SecurityRepository
): BaseUseCase<Unit, Unit?>(){
    override suspend fun invoke(params: Unit) {
     repository.enableBiometric()
    }


}