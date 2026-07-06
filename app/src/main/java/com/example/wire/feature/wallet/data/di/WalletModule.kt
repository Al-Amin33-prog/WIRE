package com.example.wire.feature.wallet.data.di



import com.example.wire.feature.wallet.data.remote.WalletApiService
import com.example.wire.feature.wallet.domain.repository.WalletRepository
import com.example.wire.feature.wallet.data.repository.WalletRepositoryImpl
import com.example.wire.feature.wallet.domain.usecase.GetBalanceUseCase
import com.example.wire.feature.wallet.domain.usecase.ObserveTransactionsUseCase
import com.example.wire.feature.wallet.domain.usecase.SyncTransactionsUseCase
import com.example.wire.feature.wallet.domain.usecase.WalletUseCases
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class WalletModule {

    @Binds
    @Singleton
    abstract fun bindWalletRepository(
        walletRepositoryImpl: WalletRepositoryImpl
    ): WalletRepository

    companion object {
        @Provides
        @Singleton
        fun provideWalletApiService(retrofit: Retrofit): WalletApiService {
            return retrofit.create(WalletApiService::class.java)
        }

        @Provides
        @Singleton
        fun provideWalletUseCases(repository: WalletRepository): WalletUseCases {
            return WalletUseCases(
                getBalance = GetBalanceUseCase(repository),
                syncTransactions = SyncTransactionsUseCase(repository),
                observeTransactions = ObserveTransactionsUseCase(repository)
            )
    }
}
}