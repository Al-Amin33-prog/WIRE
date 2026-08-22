package com.example.wire.core.feature.security.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class UploadPinRequest(
    val pinHash: String
)