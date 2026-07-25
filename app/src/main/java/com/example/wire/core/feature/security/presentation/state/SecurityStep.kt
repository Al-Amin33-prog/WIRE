package com.example.wire.core.feature.security.presentation.state

sealed interface SecurityStep {

    data object Loading : SecurityStep

    data object SetPin : SecurityStep

    data object ConfirmPin : SecurityStep

    data object EnrollBiometric : SecurityStep

    data object RequestBiometricAuthentication : SecurityStep



}