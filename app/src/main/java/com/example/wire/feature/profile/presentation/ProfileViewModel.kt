package com.example.wire.feature.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wire.core.common.util.AppError
import com.example.wire.core.common.util.Resource
import com.example.wire.core.datastore.preferences.UserPreferencesDataStore
import com.example.wire.feature.auth.domain.usecase.AuthUseCases
import com.example.wire.feature.chat.data.wrapper.ChatUseCases
import com.example.wire.feature.profile.domain.model.Profile
import com.example.wire.feature.profile.domain.usecase.ProfileUseCases
import com.example.wire.feature.profile.presentation.event.ProfileUiEvent
import com.example.wire.feature.profile.presentation.state.ProfileUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileUseCases: ProfileUseCases,
    private val authUseCases: AuthUseCases,
    private val chatUseCases: ChatUseCases,
    private val userPreferencesDataStore: UserPreferencesDataStore,

) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()



    fun onEvent(event: ProfileUiEvent) {
        when (event) {
            ProfileUiEvent.LoadProfile -> loadProfile()
            ProfileUiEvent.Refresh -> loadProfile()
            ProfileUiEvent.SaveProfile -> saveProfile()
            ProfileUiEvent.Logout -> executeCoordinatedLogout()

            is ProfileUiEvent.FullNameChanged -> _uiState.update { it.copy(fullName = event.value) }
            is ProfileUiEvent.UsernameChanged -> _uiState.update { it.copy(username = event.value) }
            is ProfileUiEvent.PhoneChanged -> _uiState.update { it.copy(phoneNumber = event.value) }
            is ProfileUiEvent.BioChanged -> _uiState.update { it.copy(bio = event.value) }
            is ProfileUiEvent.AvatarSelected -> uploadAvatar(event.image)
            is ProfileUiEvent.ToggleBiometrics -> {}
        }
    }



    private fun loadProfile() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true, error = null) }
        when (val result = profileUseCases.getProfile()) {
            is Resource.Success -> {
                val p = result.data
                _uiState.update { it.copy(
                    isLoading = false,
                    profile = p,
                    fullName = p.displayName,
                    username = p.username,
                    email = p.email,
                    phoneNumber = p.phoneNumber ?: "",
                    bio = p.bio ?: "",
                    avatarUrl = p.avatarUrl
                )}
            }
            is Resource.Error -> {
                _uiState.update { it.copy(
                    isLoading = false,
                    error = mapError(result.error)
                )}
            }
            is Resource.Loading -> {}
        }
    }

    private fun saveProfile() = viewModelScope.launch {
        _uiState.update { it.copy(isSaving = true, error = null) }
        val state = _uiState.value
        val updatedProfile = Profile(
            id = state.profile?.id.orEmpty(),
            // fullName = state.fullName,
            username = state.username,
            email = state.email,
            phoneNumber = state.phoneNumber,
            bio = state.bio,
            avatarUrl = state.avatarUrl,
            isVerified = state.profile?.isVerified ?: false,
            joinedAt = state.profile?.joinedAt ?: 0L,
            displayName = state.fullName,
            wireId = TODO(),
            totalSent = TODO(),
            totalTransfers = TODO(),
            contactCount = TODO(),
            isBiometricEnabled = TODO(),
            isPaymentPinEnabled = TODO(),
            isPushNotificationEnabled = TODO(),
            currency = TODO(),
            appVersion = TODO(),
        )

        when (val result = profileUseCases.updateProfile(updatedProfile)) {
            is Resource.Success -> {
                _uiState.update { it.copy(isSaving = false, profile = updatedProfile) }
            }
            is Resource.Error -> {
                _uiState.update { it.copy(isSaving = false, error = mapError(result.error)) }
            }
            is Resource.Loading -> {}
        }
    }

    private fun uploadAvatar(image: ByteArray) = viewModelScope.launch {
        _uiState.update { it.copy(isUploadingAvatar = true, error = null) }
        when (val result = profileUseCases.uploadAvatar(image)) {
            is Resource.Success -> {
                _uiState.update { it.copy(isUploadingAvatar = false, avatarUrl = result.data) }
            }
            is Resource.Error -> {
                _uiState.update { it.copy(isUploadingAvatar = false, error = mapError(result.error)) }
            }
            is Resource.Loading -> {}
        }
    }



    private fun executeCoordinatedLogout() = viewModelScope.launch {
        // 1. Kill WebSocket (Profiler Safety)
        chatUseCases.disconnectFromChat()
        // 2. Server Logout
        authUseCases.logout(Unit)
        // 3. Clear DataStore
        userPreferencesDataStore.clearAll()
    }

    // THE SYMPHONY ERROR MAPPER
    private fun mapError(error: AppError): String {
        return when (error) {
            is AppError.Network.NoInternet -> "Offline: Check your connection."
            is AppError.Network.Timeout -> "Server timed out. Try again."
            is AppError.Network.ServerError -> "Profile sync is currently unavailable."
            is AppError.Validation -> error.message
            is AppError.Network.Unknown -> error.message ?: "Sync failed."
            else -> "An unexpected error occurred."
        }
    }
}