package com.example.wire.core.network.interceptors


import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class AuthAuthenticator @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        // 1. Fetch a fresh token (it's okay to block here as OkHttp handles this on a background thread)
        val token = runBlocking {
            try {
                // 'true' forces a refresh of the token
                firebaseAuth.currentUser?.getIdToken(true)?.await()?.token
            } catch (e: Exception) {
                null
            }
        }

        if (token == null) return null // Give up if we can't get a token

        // 2. Retry the original request with the NEW token
        return response.request.newBuilder()
            .header("Authorization", "Bearer $token")
            .build()
    }
}