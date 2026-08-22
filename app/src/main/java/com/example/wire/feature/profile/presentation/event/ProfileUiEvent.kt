package com.example.wire.feature.profile.presentation.event





sealed class ProfileUiEvent {
    object LoadProfile : ProfileUiEvent()
    object Refresh : ProfileUiEvent()
    object SaveProfile : ProfileUiEvent()
    object Logout : ProfileUiEvent()

    data class FullNameChanged(val value: String) : ProfileUiEvent()
    data class UsernameChanged(val value: String) : ProfileUiEvent()
    data class PhoneChanged(val value: String) : ProfileUiEvent()
    data class BioChanged(val value: String) : ProfileUiEvent()
    data class AvatarSelected(val image: ByteArray) : ProfileUiEvent()
    data class ToggleBiometrics(val enabled: Boolean) : ProfileUiEvent()
}