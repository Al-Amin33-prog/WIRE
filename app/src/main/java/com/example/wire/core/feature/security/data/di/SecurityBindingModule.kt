package com.example.wire.core.feature.security.data.di

import com.example.wire.core.feature.security.data.crypto.Sha256PinHasher
import com.example.wire.core.feature.security.data.local.SecurityLocalDataSource
import com.example.wire.core.feature.security.data.local.SecurityLocalDataSourceImpl
import com.example.wire.core.feature.security.data.repository.SecurityRepositoryImpl
import com.example.wire.core.feature.security.domain.crypto.PinHasher
import com.example.wire.core.feature.security.domain.repository.SecurityRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SecurityBindingModule {
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
