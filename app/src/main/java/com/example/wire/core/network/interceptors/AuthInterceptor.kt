package com.example.wire.core.network.interceptors

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : Interceptor {

    // Inside AuthInterceptor.kt - REFACTOR THIS:
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // Use the cached token first (false = don't force refresh)
        val token = runBlocking {
            firebaseAuth.currentUser?.getIdToken(false)?.await()?.token
        }

        val requestBuilder = originalRequest.newBuilder()
        if (token != null) {
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }

        return chain.proceed(requestBuilder.build())
    }
}
