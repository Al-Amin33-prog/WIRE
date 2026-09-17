package com.example.wire.feature.profile.presentation.state

import com.example.wire.feature.profile.domain.model.Profile

data class ProfileUiState (
    val isLoading: Boolean = false,
    val isEditing: Boolean= false,
    val editDisplayName: String  = "",
    val profile: Profile? = null,
    val fullName: String = "",
    val username: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val editBio: String = "",
    val avatarUrl: String? = null,
    val isUploadingAvatar: Boolean = false,
    val isSavingProfile: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val showSignOutDialog: Boolean = false,
    val showDeleteAccountDialog: Boolean = false,
    val isSignedOut:Boolean = false,
    val isAccountDeleted: Boolean = false,
    val isBiometricEnabled: Boolean = false,
    val isSaving: Boolean = false,
    val error: String? = null
)