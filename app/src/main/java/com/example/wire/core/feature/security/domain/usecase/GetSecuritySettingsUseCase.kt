package com.example.wire.core.feature.security.domain.usecase

import com.example.wire.core.feature.security.domain.model.SecuritySettings
import com.example.wire.core.feature.security.domain.repository.SecurityRepository
import javax.inject.Inject

class GetSecuritySettingsUseCase @Inject constructor(
    private val repository: SecurityRepository
){
    suspend operator fun  invoke(): SecuritySettings{
        return repository.getSecuritySettings()
    }
}