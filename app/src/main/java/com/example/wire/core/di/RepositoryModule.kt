package com.example.wire.core.di

import com.example.wire.core.data.repository.SyncRepository
import com.example.wire.core.data.repository.SyncRepositoryImpl
import com.google.android.datatransport.runtime.dagger.Binds
import com.google.android.datatransport.runtime.dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindSyncRepository(
        syncRepositoryImpl: SyncRepositoryImpl
    ): SyncRepository
}