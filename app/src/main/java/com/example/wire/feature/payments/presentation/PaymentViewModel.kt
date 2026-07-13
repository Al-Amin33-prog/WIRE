package com.example.wire.feature.payments.presentation

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wire.core.common.util.AppError
import com.example.wire.core.common.util.PerformanceMonitor
import com.example.wire.core.common.util.Resource
import com.example.wire.core.datastore.preferences.UserPreferencesDataStore
import com.example.wire.core.ui.util.WireBiometricManager
import com.example.wire.feature.auth.domain.repository.AuthRepository
import com.example.wire.feature.payments.domain.usecase.PaymentUseCases
import com.example.wire.feature.payments.presentation.event.PaymentUiEvent
import com.example.wire.feature.payments.presentation.state.PaymentMode
import com.example.wire.feature.payments.presentation.state.PaymentUiState
import com.stripe.android.paymentsheet.PaymentSheetResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val paymentUseCases: PaymentUseCases,
    private val authRepository: AuthRepository,
    private val performanceMonitor: PerformanceMonitor,
    private val biometricManager: WireBiometricManager, // ADDED
    private val userPreferencesDataStore: UserPreferencesDataStore // ADDED
) : ViewModel() {

    private val _uiState = MutableStateFlow(PaymentUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadCurrentUserProfile()
    }

    private fun loadCurrentUserProfile() {
        viewModelScope.launch {
            val user = authRepository.getCurrentUser()
            val handle = formatDisplayNameToHandle(user?.displayName)
            _uiState.update { it.copy(currentUserHandle = handle) }
        }
    }

    fun onEvent(event: PaymentUiEvent, activity: FragmentActivity? = null) {
        when (event) {
            is PaymentUiEvent.InitPayment -> {
                _uiState.update {
                    it.copy(
                        recipientId = event.recipientId,
                        recipientName = event.recipientName,
                        paymentMode = event.mode,
                        amount = event.amount ?: it.amount
                    )
                }
            }
            is PaymentUiEvent.NumberClicked -> handleNumberInput(event.number)
            PaymentUiEvent.BackspaceClicked -> {
                if (_uiState.value.amount.isNotEmpty()) {
                    _uiState.update { it.copy(amount = it.amount.dropLast(1)) }
                }
            }
            is PaymentUiEvent.NoteChanged -> {
                _uiState.update { it.copy(note = event.note) }
            }
            is PaymentUiEvent.PreparePayment -> {
                _uiState.update { it.copy(
                    showConfirmSheet = true
                )}
            }
            is PaymentUiEvent.TabChanged -> {
                _uiState.update { it.copy(selectedTab = event.tab) }
            }


            // THE FIX: Wire the security gate here instead of calling initiateTransaction directly
            // In PaymentViewModel.kt
            PaymentUiEvent.ConfirmClicked -> {
                // FIX: Use == for comparison, not = for assignment
                if (_uiState.value.paymentMode == PaymentMode.SEND) {
                    activity?.let { onConfirmPaymentClicked(it) }
                } else {
                    createRequest()
                }
            }


            PaymentUiEvent.DismissSheet -> {
                _uiState.update { it.copy(showConfirmSheet = false, error = null) }
            }
            PaymentUiEvent.ResetPayment -> _uiState.value = PaymentUiState()
            else -> {}
        }
    }
    private fun createRequest(){
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            // SYMPHONY: This sends a "REQUEST_PAYMENT" action to the backend
            // Your PaymentProcessor on the OTHER side will catch this and show a notification
            val result = paymentUseCases.confirmPayment( // We can reuse UseCase or make specific one
                _uiState.value.recipientId,
                "REQUEST:${_uiState.value.amount}"
            )

            if (result is Resource.Success) {
                _uiState.update { it.copy(isLoading = false, isPaymentSuccessful = true) }
            }
        }

        }



    private fun handleNumberInput(number: String) {
        val current = _uiState.value.amount
        if (number == "." && current.contains(".")) return
        if (current.contains(".") && current.substringAfter(".").length >= 2) return
        _uiState.update { it.copy(amount = current + number) }
    }

    private fun initiateTransaction() {
        val state = _uiState.value
        val amountDouble = state.amount.toDoubleOrNull() ?: 0.0
        val startTime = System.currentTimeMillis()

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val result = paymentUseCases.createIntent(
                amount = amountDouble,
                recipientId = state.recipientId,
                note = state.note
            )

            when (result) {
                is Resource.Success -> {
                    performanceMonitor.recordEvent(System.currentTimeMillis() - startTime)
                    _uiState.update { it.copy(
                        isLoading = false,
                        showConfirmSheet = false,
                        paymentIntent = result.data,
                        stripeClientSecret = result.data.clientSecret,
                        stripePublishableKey = result.data.publishableKey
                    )}
                }
                is Resource.Error -> {
                    _uiState.update { it.copy(
                        isLoading = false,
                        error = mapError(result.error)
                    )}
                }
                is Resource.Loading -> { }
            }
        }
    }

    fun onPaymentResult(paymentResult: PaymentSheetResult) {
        when (paymentResult) {
            is PaymentSheetResult.Completed -> {
                viewModelScope.launch {
                    val result = paymentUseCases.confirmPayment(_uiState.value.recipientId, "SUCCESS")
                    if (result is Resource.Success) {
                        _uiState.update { it.copy(isPaymentSuccessful = true) }
                    } else if (result is Resource.Error) {
                        _uiState.update { it.copy(error = mapError(result.error)) }
                    }
                }
            }
            is PaymentSheetResult.Failed -> {
                _uiState.update { it.copy(error = "Payment failed: ${paymentResult.error.localizedMessage}") }
            }
            is PaymentSheetResult.Canceled -> {
                _uiState.update { it.copy(error = "Payment cancelled") }
            }
        }
    }

    // THE FIX: Use UserPreferencesDataStore and wire biometricManager correctly
    private fun onConfirmPaymentClicked(activity: FragmentActivity) {
        val state = _uiState.value

        viewModelScope.launch {
            // 1. Get value from DataStore (Single Source of Truth for settings)
            val isSecurityEnabled = userPreferencesDataStore.isBiometricEnabled.first()

            if (isSecurityEnabled) {
                biometricManager.showBiometricPrompt(
                    activity = activity,
                    title = "Confirm Payment",
                    subtitle = "Pay $${state.amount} to ${state.recipientName}",
                    onSuccess = {
                        initiateTransaction()
                    },
                    onError = { error ->
                        _uiState.update { it.copy(error = error) }
                    },
                    onFailed = {
                        _uiState.update { it.copy(error = "Authentication failed") }
                    }
                )
            } else {
                // If not enabled, proceed to transaction (or force a PIN/Password screen here)
                initiateTransaction()
            }
        }
    }

    private fun formatDisplayNameToHandle(name: String?): String {
        return name?.lowercase()?.trim()?.replace(" ", ".") ?: "user"
    }

    private fun mapError(error: AppError): String {
        return when (error) {
            is AppError.Network.NoInternet -> "Check your connection to send money."
            is AppError.Network.Timeout -> "Server is busy. Trying again..."
            is AppError.Network.ServerError -> "Our vault is currently under maintenance."
            is AppError.Validation -> error.message
            is AppError.Network.Unknown -> error.message ?: "Transaction could not be initialized."
            else -> "An unexpected error occurred."
        }
    }
}