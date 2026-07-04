package com.example.wire.core.di



import com.example.wire.core.worker.WorkScheduler
import com.example.wire.core.worker.WorkSchedulerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class WorkerModule {

    @Binds
    @Singleton
    abstract fun bindWorkScheduler(
        workSchedulerImpl: WorkSchedulerImpl
    ): WorkScheduler
}