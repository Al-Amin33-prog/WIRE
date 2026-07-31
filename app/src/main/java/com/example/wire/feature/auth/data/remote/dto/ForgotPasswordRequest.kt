package com.example.wire.feature.auth.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ForgotPasswordRequest(
    val email: String
)