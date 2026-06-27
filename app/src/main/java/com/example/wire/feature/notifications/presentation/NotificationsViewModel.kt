package com.example.wire.feature.notifications.presentation

import com.example.wire.feature.notifications.domain.usecase.NotificationUseCases
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wire.feature.notifications.presentation.state.NotificationUIState
import com.example.wire.feature.notifications.presentation.event.NotificationUIEvent
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
        // Start observing notifications as soon as the ViewModel is created
        observeNotifications()
    }

    private fun observeNotifications() {
        _uiState.update { it.copy(isLoading = true) }
        useCases.getNotifications()
            .onEach { list ->
                _uiState.update { it.copy(notifications = list, isLoading = false) }
            }
            .catch { e ->
                _uiState.update { it.copy(error = e.message, isLoading = false) }
            }
            .launchIn(viewModelScope)
    }

    fun onEvent(event: NotificationUIEvent) {
        viewModelScope.launch {
            when(event) {
                is NotificationUIEvent.Refresh -> observeNotifications()
                is NotificationUIEvent.MarkAsRead -> {
                    useCases.markAsRead(event.id)
                }
                is NotificationUIEvent.ClearAll -> {
                    useCases.clearAll()
                }
            }
        }
    }
}
