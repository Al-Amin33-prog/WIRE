package com.example.wire.feature.auth.data.di

import com.example.wire.feature.auth.data.remote.authApiServices.AuthApiService
import com.example.wire.feature.auth.data.repository.AuthRepositoryImpl
import com.example.wire.feature.auth.domain.repository.AuthRepository
import com.example.wire.feature.auth.domain.usecase.AuthUseCases
import com.example.wire.feature.auth.domain.usecase.CreateAccountUseCase
import com.example.wire.feature.auth.domain.usecase.ForgotPasswordUseCase
import com.example.wire.feature.auth.domain.usecase.GetCurrentUserUseCase
import com.example.wire.feature.auth.domain.usecase.GoogleSignInUseCase
import com.example.wire.feature.auth.domain.usecase.LoginUseCase
import com.example.wire.feature.auth.domain.usecase.LogoutUseCase
import com.example.wire.feature.auth.domain.usecase.ObserveAuthStateUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {

    @Binds
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository

    companion object {

        @Provides
        @Singleton
        fun provideAuthUseCases(repository: AuthRepository): AuthUseCases {
            return AuthUseCases(
                login = LoginUseCase(repository),
                createAccount = CreateAccountUseCase(repository),
                logout = LogoutUseCase(repository),
                observeAuthState = ObserveAuthStateUseCase(repository),
                forgotPassword = ForgotPasswordUseCase(repository),
                googleSignIn = GoogleSignInUseCase(repository),
                getCurrentUser = GetCurrentUserUseCase(repository)
            )
        }

        @Provides
        @Singleton
        fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

        @Provides
        fun provideAuthApiService(retrofit: Retrofit): AuthApiService =
            retrofit.create(AuthApiService::class.java)
    }
}