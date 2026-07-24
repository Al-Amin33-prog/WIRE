package com.example.wire.core.feature.security.domain.usecase

import com.example.wire.core.domain.base.BaseUseCase
import com.example.wire.core.feature.security.domain.repository.SecurityRepository
import javax.inject.Inject

class CreatePinUseCase @Inject constructor (
    private val repository: SecurityRepository
): BaseUseCase<String, Unit>(){
    override suspend fun invoke(params: String) {
        require(params.length == 4){
            "PIN must contain 4 digits"
        }
        repository.createPin(params)
    }
}