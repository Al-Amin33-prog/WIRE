package com.example.wire.feature.notifications.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wire.core.common.util.AppError
import com.example.wire.core.common.util.Resource
import com.example.wire.feature.notifications.domain.usecase.NotificationUseCases
import com.example.wire.feature.notifications.presentation.event.NotificationUIEvent
import com.example.wire.feature.notifications.presentation.state.NotificationUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val useCases: NotificationUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationUIState())
    val uiState = _uiState.asStateFlow()

    init {
        observeNotifications()
    }

    private fun observeNotifications() {
        useCases.getNotifications()
            .onEach { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                notifications = resource.data, // Access .data to get the List
                                isLoading = false,
                                error = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                error = mapError(resource.error),
                                isLoading = false
                            )
                        }
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    fun onEvent(event: NotificationUIEvent) {
        viewModelScope.launch {
            when(event) {
                is NotificationUIEvent.Refresh -> observeNotifications()
                is NotificationUIEvent.MarkAsRead -> {
                    // Handle the Resource result from the suspend call
                    val result = useCases.markAsRead(event.id)
                    if (result is Resource.Error) {
                        _uiState.update { it.copy(error = mapError(result.error)) }
                    }
                }
                is NotificationUIEvent.ClearAll -> {
                    // Handle the Resource result from the suspend call
                    val result = useCases.clearAll()
                    if (result is Resource.Error) {
                        _uiState.update { it.copy(error = mapError(result.error)) }
                    }
                }
            }
        }
    }

    // Standardized error mapping (Matches Auth and Chat)
    private fun mapError(error: AppError): String {
        return when (error) {
            is AppError.Validation -> error.message
            is AppError.Network.NoInternet -> "No internet connection."
            is AppError.Network.Timeout -> "The server took too long to respond."
            is AppError.Network.Unknown -> error.message ?: "An unexpected error occurred"
            else -> "Something went wrong"
        }
    }
}