package com.example.wire.feature.profile.presentation.state

import com.example.wire.feature.profile.domain.model.Profile

data class ProfileUiState (
    val isLoading: Boolean = false,
    val profile: Profile? = null,
    val fullName: String = "",
    val username: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val bio: String = "",
    val avatarUrl: String? = null,
    val isUploadingAvatar: Boolean = false,
    val isSavingProfile: Boolean = false,
    val errorMessage: String? = null,
    val isBiometricEnabled: Boolean = false,
    val isSaving: Boolean = false,
    val error: String? = null
)