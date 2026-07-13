package com.example.wire.feature.payments.data.remote


import kotlinx.serialization.Serializable
import retrofit2.http.POST

@Serializable
data class StripeCustomerResponse(
    val customerId: String,
    val ephemeralKey: String,
    val publishableKey: String
)

interface StripeApiService {
    @POST("api/payments/stripe-customer")
    suspend fun getStripeCustomerContext(): StripeCustomerResponse
}