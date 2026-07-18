package com.example.wire.core.feature.security.data.di

import com.example.wire.core.feature.security.data.crypto.Sha256PinHasher
import com.example.wire.core.feature.security.data.local.SecurityLocalDataSource
import com.example.wire.core.feature.security.data.repository.SecurityLocalDataSourceImpl
import com.example.wire.core.feature.security.data.repository.SecurityRepositoryImpl
import com.example.wire.core.feature.security.domain.crypto.PinHasher
import com.example.wire.core.feature.security.domain.repository.SecurityRepository
import com.example.wire.core.feature.security.domain.usecase.CreatePinUseCase
import com.example.wire.core.feature.security.domain.usecase.DisableBiometricUseCase
import com.example.wire.core.feature.security.domain.usecase.EnableBiometricUseCase
import com.example.wire.core.feature.security.domain.usecase.GetSecuritySettingsUseCase
import com.example.wire.core.feature.security.domain.usecase.HaspInUseCase
import com.example.wire.core.feature.security.domain.usecase.SecurityUseCases
import com.example.wire.core.feature.security.domain.usecase.VerifyPinUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SecurityModule {
    @Provides
    @Singleton
    fun provideSecurityUseCases(
        createPin: CreatePinUseCase,
        verifyPin: VerifyPinUseCase,
        hasPin: HaspInUseCase,
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


    @Binds
    @Singleton
    abstract fun bindPinHasher(
        impl: Sha256PinHasher
    ): PinHasher

    @Binds
    @Singleton
    abstract fun bindSecurityLocalDataSource(
        impl: SecurityLocalDataSourceImpl
    ): SecurityLocalDataSource

    @Binds
    @Singleton
    abstract  fun bindSecurityRepository(
        impl: SecurityRepositoryImpl
    ): SecurityRepository

}