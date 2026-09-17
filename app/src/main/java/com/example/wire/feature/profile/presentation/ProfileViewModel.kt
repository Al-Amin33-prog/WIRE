package com.example.wire.feature.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wire.feature.profile.domain.usecase.DeleteAccountUseCase
import com.example.wire.feature.profile.domain.usecase.GetProfileUseCase
import com.example.wire.feature.profile.domain.usecase.SignOutUseCase
import com.example.wire.feature.profile.domain.usecase.UpdateProfileUseCase
import com.example.wire.feature.profile.domain.usecase.UpdateSecuritySettingsUseCase
import com.example.wire.feature.profile.presentation.event.ProfileUiEvent
import com.example.wire.feature.profile.presentation.state.ProfileUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val updateSecuritySettingsUseCase: UpdateSecuritySettingsUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val deleteAccountUseCase: DeleteAccountUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        observeProfile()
    }

    fun onEvent(event: ProfileUiEvent) {
        when (event) {
            ProfileUiEvent.EditClicked -> startEditing()
            ProfileUiEvent.CancelEditClicked -> cancelEditing()
            ProfileUiEvent.SaveProfileClicked -> saveProfile()

            is ProfileUiEvent.DisplayNameChanged ->
                _uiState.update { it.copy(editDisplayName = event.value) }

            is ProfileUiEvent.BioChanged ->
                _uiState.update { it.copy(editBio = event.value) }

            is ProfileUiEvent.BiometricToggled -> updateSecurity(
                biometricEnabled = event.enabled
            )
            is ProfileUiEvent.PaymentPinToggled -> updateSecurity(
                paymentPinEnabled = event.enabled
            )
            is ProfileUiEvent.NotificationsToggled -> updateSecurity(
                pushNotificationsEnabled = event.enabled
            )

            ProfileUiEvent.SignOutClicked ->
                _uiState.update { it.copy(showSignOutDialog = true) }

            ProfileUiEvent.SignOutConfirmed -> signOut()

            ProfileUiEvent.SignOutDismissed ->
                _uiState.update { it.copy(showSignOutDialog = false) }

            ProfileUiEvent.DeleteAccountClicked ->
                _uiState.update { it.copy(showDeleteAccountDialog = true) }

            ProfileUiEvent.DeleteAccountConfirmed -> deleteAccount()

            ProfileUiEvent.DeleteAccountDismissed ->
                _uiState.update { it.copy(showDeleteAccountDialog = false) }

            ProfileUiEvent.ErrorDismissed ->
                _uiState.update { it.copy(errorMessage = null) }

            ProfileUiEvent.SuccessDismissed ->
                _uiState.update { it.copy(successMessage = null) }
        }
    }

    private fun observeProfile() {
        viewModelScope.launch {
            getProfileUseCase(Unit).collect { profile ->
                _uiState.update { it.copy(profile = profile) }
            }
        }
    }

    private fun startEditing() {
        val profile = _uiState.value.profile ?: return
        _uiState.update {
            it.copy(
                isEditing = true,
                editDisplayName = profile.displayName,
                editBio = profile.bio ?: ""
            )
        }
    }

    private fun cancelEditing() {
        _uiState.update {
            it.copy(isEditing = false, editDisplayName = "", editBio = "")
        }
    }

    private fun saveProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val result = updateProfileUseCase(
                UpdateProfileUseCase.Params(
                    displayName = _uiState.value.editDisplayName,
                    bio = _uiState.value.editBio
                )
            )

            result.fold(
                onSuccess = {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isEditing = false,
                            successMessage = "Profile updated successfully"
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Failed to update profile"
                        )
                    }
                }
            )
        }
    }

    private fun updateSecurity(
        biometricEnabled: Boolean? = null,
        paymentPinEnabled: Boolean? = null,
        pushNotificationsEnabled: Boolean? = null
    ) {
        viewModelScope.launch {
            updateSecuritySettingsUseCase(
                UpdateSecuritySettingsUseCase.Params(
                    biometricEnabled = biometricEnabled,
                    paymentPinEnabled = paymentPinEnabled,
                    pushNotificationsEnabled = pushNotificationsEnabled
                )
            )
        }
    }

    private fun signOut() {
        viewModelScope.launch {
            signOutUseCase(Unit)
            _uiState.update {
                it.copy(
                    showSignOutDialog = false,
                    isSignedOut = true
                )
            }
        }
    }

    private fun deleteAccount() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val result = deleteAccountUseCase(Unit)

            result.fold(
                onSuccess = {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            showDeleteAccountDialog = false,
                            isAccountDeleted = true
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            showDeleteAccountDialog = false,
                            errorMessage = error.message ?: "Failed to delete account"
                        )
                    }
                }
            )
        }
    }
}