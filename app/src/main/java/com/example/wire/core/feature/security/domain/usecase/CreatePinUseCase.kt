package com.example.wire.core.feature.security.domain.usecase

import com.example.wire.core.feature.security.domain.repository.SecurityRepository
import javax.inject.Inject

class CreatePinUseCase @Inject constructor (
    private val repository: SecurityRepository
){
    suspend  operator fun invoke(pin:String){
        repository.createPin(pin)
    }
}