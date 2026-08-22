package com.example.wire.feature.profile.data.di

import com.example.wire.feature.profile.data.remote.ProfileApiService
import com.example.wire.feature.profile.data.repository.ProfileRepositoryImpl
import com.example.wire.feature.profile.domain.repository.ProfileRepository
import com.example.wire.feature.profile.domain.usecase.GetProfileUseCase
import com.example.wire.feature.profile.domain.usecase.ProfileUseCases
import com.example.wire.feature.profile.domain.usecase.UpdateProfileUseCase
import com.example.wire.feature.profile.domain.usecase.UploadAvatarUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ProfileModule {

    // THE FIX: @Binds must be a direct abstract member of the class, NOT the companion object
    @Binds
    @Singleton
    abstract fun bindProfileRepository(
        profileRepositoryImpl: ProfileRepositoryImpl
    ): ProfileRepository

    companion object {
        @Provides
        @Singleton
        fun provideProfileApiService(retrofit: Retrofit): ProfileApiService {
            return retrofit.create(ProfileApiService::class.java)
        }

        @Provides
        @Singleton
        fun provideProfileUseCases(repository: ProfileRepository): ProfileUseCases {
            return ProfileUseCases(
                getProfile = GetProfileUseCase(repository),
                updateProfile = UpdateProfileUseCase(repository),
                uploadAvatar = UploadAvatarUseCase(repository)
            )
        }
    }
}