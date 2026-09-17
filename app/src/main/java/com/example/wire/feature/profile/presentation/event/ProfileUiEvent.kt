package com.example.wire.feature.profile.presentation.event

sealed class ProfileUiEvent {
    object EditClicked : ProfileUiEvent()
    object CancelEditClicked : ProfileUiEvent()
    object SaveProfileClicked : ProfileUiEvent()

    data class DisplayNameChanged(val value: String) : ProfileUiEvent()
    data class BioChanged(val value: String) : ProfileUiEvent()
    data class BiometricToggled(val enabled: Boolean) : ProfileUiEvent()
    data class PaymentPinToggled(val enabled: Boolean) : ProfileUiEvent()
    data class NotificationsToggled(val enabled: Boolean) : ProfileUiEvent()

    object SignOutClicked : ProfileUiEvent()
    object SignOutConfirmed : ProfileUiEvent()
    object SignOutDismissed : ProfileUiEvent()

    object DeleteAccountClicked : ProfileUiEvent()
    object DeleteAccountConfirmed : ProfileUiEvent()
    object DeleteAccountDismissed : ProfileUiEvent()

    object ErrorDismissed : ProfileUiEvent()
    object SuccessDismissed : ProfileUiEvent()
}