package com.example.wire.core.feature.security.domain.usecase

import com.example.wire.core.domain.base.BaseUseCase
import com.example.wire.core.feature.security.domain.repository.SecurityRepository
import javax.inject.Inject

class VerifyPinUseCase @Inject constructor (
    private val repository: SecurityRepository
): BaseUseCase<String, Boolean>(){
    override suspend fun invoke(params: String): Boolean {
        require(params.length == 4){
            "PIN must contain exactly 4 digits"
        }
        return repository.verifyPin(params)
    }

}