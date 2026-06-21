package com.example.wire.feature.auth.domain.model

data class AuthUser(
    val uid: String,
    val email: String,
    val displayName: String?,
    val isEmailVerified: Boolean,
    val token: String,
    val phone: String
)