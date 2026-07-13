package com.example.wire.feature.payments.data.remote



import com.example.wire.feature.payments.data.remote.dto.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PaymentApiService {

    @POST("api/payments/create-intent")
    suspend fun createPaymentIntent(
        @Body request: CreatePaymentRequest
    ): PaymentIntentResponse

    @POST("api/payments/confirm")
    suspend fun confirmPayment(
        @Body request: ConfirmPaymentRequest
    )

    @GET("api/payments/status/{paymentId}")
    suspend fun getPaymentStatus(
        @Path("paymentId") paymentId: String
    ): String
}