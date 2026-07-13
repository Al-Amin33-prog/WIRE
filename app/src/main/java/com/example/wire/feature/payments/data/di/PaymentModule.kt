package com.example.wire.feature.payments.data.di

import com.example.wire.core.database.dao.TransactionDao
import com.example.wire.core.network.websocket.WebSocketProcessor
import com.example.wire.feature.payments.data.processor.PaymentProcessor
import com.example.wire.feature.payments.data.remote.PaymentApiService
import com.example.wire.feature.payments.data.remote.StripeApiService
import com.example.wire.feature.payments.data.repository.PaymentRepositoryImpl
import com.example.wire.feature.payments.data.util.IdempotencyKeyGenerator
import com.example.wire.feature.payments.data.util.IdempotencyKeyGeneratorImpl
import com.example.wire.feature.payments.domain.repository.PaymentRepository
import com.example.wire.feature.payments.domain.usecase.ConfirmPaymentUseCase
import com.example.wire.feature.payments.domain.usecase.CreatePaymentIntentUseCase
import com.example.wire.feature.payments.domain.usecase.PaymentUseCases
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PaymentModule {

    @Binds
    abstract fun bindPaymentRepository(impl: PaymentRepositoryImpl): PaymentRepository

    @Binds
    @Singleton
    abstract fun bindIdempotencyKeyGenerator(impl: IdempotencyKeyGeneratorImpl): IdempotencyKeyGenerator

    companion object {
        @Provides
        @Singleton
        fun providePaymentApiService(retrofit: Retrofit): PaymentApiService =
            retrofit.create(PaymentApiService::class.java)

        @Provides
        @IntoSet
        fun providePaymentProcessor(transactionDao: TransactionDao): WebSocketProcessor =
            PaymentProcessor(transactionDao)

        @Provides
        @Singleton
        fun providePaymentUseCases(repository: PaymentRepository): PaymentUseCases =
            PaymentUseCases(
                createIntent = CreatePaymentIntentUseCase(repository),
                confirmPayment = ConfirmPaymentUseCase(repository)
            )


            @Provides
            @Singleton
            fun provideStripeApiService(retrofit: Retrofit): StripeApiService =
                retrofit.create(StripeApiService::class.java)





    }
}