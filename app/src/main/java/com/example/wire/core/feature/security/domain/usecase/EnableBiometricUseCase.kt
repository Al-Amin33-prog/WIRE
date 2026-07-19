package com.example.wire.core.feature.security.domain.usecase

import com.example.wire.core.feature.security.domain.repository.SecurityRepository
import javax.inject.Inject

class EnableBiometricUseCase @Inject constructor(
    private val repository: SecurityRepository
){
    suspend  operator fun invoke(){
        repository.enableBiometric()
    }
}