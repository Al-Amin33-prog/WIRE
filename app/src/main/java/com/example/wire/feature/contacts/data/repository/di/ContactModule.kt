package com.example.wire.feature.contacts.data.repository.di



import com.example.wire.feature.contacts.data.repository.ContactRepository
import com.example.wire.feature.contacts.data.repository.ContactRepositoryImpl

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ContactModule {

    @Binds
    @Singleton
    abstract fun bindContactRepository(
        contactRepositoryImpl: ContactRepositoryImpl
    ): ContactRepository
}