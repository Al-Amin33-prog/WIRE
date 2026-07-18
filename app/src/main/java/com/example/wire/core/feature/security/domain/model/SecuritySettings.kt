package com.example.wire.core.feature.security.domain.model

data class SecuritySettings(
    val isBiometricEnabled: Boolean,
    val hasPin: Boolean
)