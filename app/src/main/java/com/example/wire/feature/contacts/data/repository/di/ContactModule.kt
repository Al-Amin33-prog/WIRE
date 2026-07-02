package com.example.wire.feature.contacts.data.repository.di

import com.example.wire.feature.contacts.data.repository.ContactRepository
import com.example.wire.feature.contacts.data.repository.ContactRepositoryImpl
import com.example.wire.feature.contacts.data.repository.remote.ContactApiService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ContactModule {

    @Binds
    @Singleton
    abstract fun bindContactRepository(
        contactRepositoryImpl: ContactRepositoryImpl
    ): ContactRepository

    companion object {
        @Provides
        @Singleton
        fun provideContactApiService(retrofit: Retrofit): ContactApiService {
            return retrofit.create(ContactApiService::class.java)
        }
    }
}
