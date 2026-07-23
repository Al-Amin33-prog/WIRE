package com.example.wire.core.feature.security.data.di


import com.example.wire.core.feature.security.domain.usecase.CreatePinUseCase
import com.example.wire.core.feature.security.domain.usecase.DisableBiometricUseCase
import com.example.wire.core.feature.security.domain.usecase.EnableBiometricUseCase
import com.example.wire.core.feature.security.domain.usecase.GetSecuritySettingsUseCase
import com.example.wire.core.feature.security.domain.usecase.HasPinUseCase
import com.example.wire.core.feature.security.domain.usecase.SecurityUseCases
import com.example.wire.core.feature.security.domain.usecase.VerifyPinUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SecurityProvideModule  {



    @Provides
    @Singleton
    fun provideSecurityUseCases(
        createPin: CreatePinUseCase,
        verifyPin: VerifyPinUseCase,
        hasPin: HasPinUseCase,
        enableBiometric: EnableBiometricUseCase,
        disableBiometric: DisableBiometricUseCase,
        getSecuritySettings: GetSecuritySettingsUseCase,

    ): SecurityUseCases{
        return SecurityUseCases(
            createPin,
            verifyPin,
            hasPin,
            enableBiometric,
            disableBiometric,
            getSecuritySettings
        )
    }




}