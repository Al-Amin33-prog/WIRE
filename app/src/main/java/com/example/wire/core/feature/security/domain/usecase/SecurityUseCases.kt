package com.example.wire.core.feature.security.domain.usecase

import javax.inject.Inject

data class SecurityUseCases @Inject constructor(
    val createPin: CreatePinUseCase,
    val verifyPin: VerifyPinUseCase,
    val hasPin: HasPinUseCase,
    val enableBiometric: EnableBiometricUseCase,
    val disableBiometric: DisableBiometricUseCase,
    val getSecuritySettings: GetSecuritySettingsUseCase
)