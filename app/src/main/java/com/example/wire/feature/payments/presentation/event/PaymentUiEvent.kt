package com.example.wire.feature.payments.presentation.event

import com.example.wire.feature.payments.presentation.state.PaymentMode
import com.example.wire.feature.payments.presentation.state.PaymentTab


sealed class PaymentUiEvent {
    // Numpad Events
    data class NumberClicked(val number: String) : PaymentUiEvent()
    object BackspaceClicked : PaymentUiEvent()

    // Action Events
    data class NoteChanged(val note: String) : PaymentUiEvent()
    data class ManualHandleChanged(val handle: String) : PaymentUiEvent()
    object PreparePayment : PaymentUiEvent()
    object ConfirmClicked : PaymentUiEvent()
    object DismissSheet : PaymentUiEvent()
    object ResetPayment : PaymentUiEvent()
    data class TabChanged(val tab: PaymentTab) : PaymentUiEvent()
    data class InitPayment(
        val recipientId: String,
        val recipientName: String,
        val mode: PaymentMode,
        val amount: String? = null
    ) : PaymentUiEvent()
}