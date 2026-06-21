package com.example.wire.feature.auth.data.remote

import com.example.wire.feature.auth.data.remote.dto.AuthUserDto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthDataSource @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {

    suspend fun login(email: String, password: String): AuthUserDto {
        val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
        val user = result.user ?: throw IllegalStateException("Login failed — no user returned")
        return user.toDto()
    }

    suspend fun register(email: String, password: String, displayName: String, phone: String): AuthUserDto {
        val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        val user = result.user ?: throw IllegalStateException("Registration failed — no user returned")

        val profileUpdate = com.google.firebase.auth.UserProfileChangeRequest.Builder()
            .setDisplayName(displayName)
            .build()
        user.updateProfile(profileUpdate).await()

        return user.toDto()
    }

    fun logout() {
        firebaseAuth.signOut()
    }

    fun observeAuthState() = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser?.toDto())
        }
        firebaseAuth.addAuthStateListener(listener)
        awaitClose { firebaseAuth.removeAuthStateListener(listener) }
    }

    fun getCurrentUser(): AuthUserDto? {
        return firebaseAuth.currentUser?.toDto()
    }

    suspend fun sendPasswordResetEmail(email: String) {
        firebaseAuth.sendPasswordResetEmail(email).await()
    }

    private fun com.google.firebase.auth.FirebaseUser.toDto(): AuthUserDto {
        return AuthUserDto(
            uid = uid,
            email = email,
            displayName = displayName,
            isEmailVerified = isEmailVerified
        )
    }
    suspend fun loginWithGoogle(idToken: String): AuthUserDto {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        val result = firebaseAuth.signInWithCredential(credential).await()
        val user = result.user
            ?: throw IllegalStateException("Google sign-in failed — no user returned")
        return user.toDto()
    }
}