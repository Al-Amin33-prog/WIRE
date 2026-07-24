package com.example.wire.core.feature.security.domain.usecase

import com.example.wire.core.domain.base.BaseUseCase
import com.example.wire.core.feature.security.domain.model.SecuritySettings
import com.example.wire.core.feature.security.domain.repository.SecurityRepository
import javax.inject.Inject

class GetSecuritySettingsUseCase @Inject constructor(
    private val repository: SecurityRepository
): BaseUseCase<Unit, SecuritySettings>(){
    override suspend fun invoke(params: Unit): SecuritySettings {
        return repository.getSecuritySettings()
    }
}