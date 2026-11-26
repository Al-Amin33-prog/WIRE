package com.example.wire.feature.profile.data.mapper

import com.example.wire.core.database.entity.UserEntity
import com.example.wire.feature.profile.data.remote.dto.ProfileDto
import com.example.wire.feature.profile.domain.model.Profile
import com.google.firebase.auth.FirebaseUser

fun ProfileDto.toDomain(
    isBiometricEnabled: Boolean = false,
    isPaymentPinEnabled: Boolean = false,
    isPushNotificationsEnabled: Boolean = true
): Profile = Profile(
    uid = uid,
    displayName = displayName,
    email = email,
    phoneNumber = phoneNumber,
    wireId = wireId,
    avatarUrl = avatarUrl,
    bio = bio,
    isKycVerified = isKycVerified,
    totalSent = totalSent,
    totalReceived = totalReceived,
    totalTransfers = totalTransfers,
    contactCount = contactCount,
    isBiometricEnabled = isBiometricEnabled,
    isPaymentPinEnabled = isPaymentPinEnabled,
    isPushNotificationsEnabled = isPushNotificationsEnabled,
    currency = "USD",
    appVersion = appVersion,

    joinedAt = joinedAt,
    username = userName
)

fun UserEntity.toDomain(
    isBiometricEnabled: Boolean = false,
    isPaymentPinEnabled: Boolean = false,
    isPushNotificationsEnabled: Boolean = true
): Profile = Profile(
    uid = id,
    displayName = fullName,
    email = email,
    phoneNumber = phoneNumber,
    wireId = "WIRE·${id.take(8).uppercase()}",
    avatarUrl = avatarUrl,
    bio = bio,
    isKycVerified = isVerified,
    totalSent = 0.0,
    totalReceived = 0.0,
    totalTransfers = 0,
    contactCount = 0,
    isBiometricEnabled = isBiometricEnabled,
    isPaymentPinEnabled = isPaymentPinEnabled,
    isPushNotificationsEnabled = isPushNotificationsEnabled,
    currency = "USD",
    appVersion = "1.0.0",
    joinedAt = joinedAt,
    username = username
)

// Maps Firebase user to a local Profile
// Used when backend call fails but Firebase session exists
fun FirebaseUser.toLocalProfile(): Profile = Profile(
    uid = uid,
    displayName = displayName ?: "Wire User",
    email = email ?: "",
    phoneNumber = phoneNumber,
    wireId = "WIRE·${uid.take(8).uppercase()}",
    avatarUrl = photoUrl?.toString(),
    bio = null,
    isKycVerified = false,
    totalSent = 0.0,
    totalReceived = 0.0,
    totalTransfers = 0,
    contactCount = 0,
    isBiometricEnabled = false,
    isPaymentPinEnabled = false,
    isPushNotificationsEnabled = true,
    currency = "USD",
    appVersion = "1.0.0",
    joinedAt = 20,
    username = ""

)