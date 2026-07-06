package com.example.wire.feature.wallet.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wire.core.common.util.AppError
import com.example.wire.core.common.util.Resource
import com.example.wire.feature.wallet.domain.usecase.WalletUseCases
import com.example.wire.feature.wallet.presentation.event.WalletUiEvent
import com.example.wire.feature.wallet.presentation.state.WalletUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WalletViewModel @Inject constructor(
    private val walletUseCases: WalletUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(WalletUiState())
    val uiState = _uiState.asStateFlow()

    init {

        observeTransactions()
        // 2. Fetch fresh data from network
        refresh()
    }

    fun onEvent(event: WalletUiEvent) {
        when (event) {
            is WalletUiEvent.Refresh -> refresh()
            is WalletUiEvent.LoadTransactionDetails -> { /* logic for detail screen */ }
        }
    }

    private fun observeTransactions() {
        walletUseCases.observeTransactions()
            .onEach { transactionList ->
                _uiState.update { it.copy(transactions = transactionList) }
            }
            .launchIn(viewModelScope)
    }

    private fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            // 1. Handle Balance Result
            when (val result = walletUseCases.getBalance()) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            balance = result.data,
                            isLoading = false
                        )
                    }
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            error = mapError(result.error),
                            isLoading = false
                        )
                    }
                }
                is Resource.Loading -> { /* Handled manually */ }
            }

            // 2. Trigger Background Sync for History
            // We don't wait for this to update the UI; Room observations will handle it.
            walletUseCases.syncTransactions()
        }
    }

    /**
     * Standardized error mapping for the Symphony architecture.
     */
    private fun mapError(error: AppError): String {
        return when (error) {
            is AppError.Network.NoInternet -> "No connection. Balance might be outdated."
            is AppError.Network.Timeout -> "Server timed out. Retrying..."
            is AppError.Network.Unknown -> error.message ?: "Could not update wallet."
            is AppError.Validation -> error.message
            else -> "Something went wrong."
        }
    }
}