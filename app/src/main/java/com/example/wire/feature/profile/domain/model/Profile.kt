package com.example.wire.feature.profile.domain.model

data class Profile(
    val id: String,
    val displayName: String,
    val username : String,
    val email: String,
    val phoneNumber: String?,
    val bio: String?,
    val avatarUrl: String?,
    val isVerified: Boolean,
    val joinedAt: Long,
    val wireId: String,
    val totalSent: Double,
    val totalTransfers: Int,
    val contactCount: Int,
    val isBiometricEnabled: Boolean,
    val isPaymentPinEnabled: Boolean,
    val isPushNotificationEnabled: Boolean,
    val currency: String,
    val appVersion: String
)