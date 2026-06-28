package com.example.wire.feature.contacts.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wire.feature.contacts.data.repository.ContactRepository
import com.example.wire.feature.contacts.domain.model.usescase.GetContactsUseCase // Updated

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactViewModel @Inject constructor(
    private val getContactsUseCase: GetContactsUseCase,
    private val repository: ContactRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ContactUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadContacts()
    }

    private fun loadContacts() {
        // Use the UseCase here
        getContactsUseCase()
            .onStart { _uiState.update { it.copy(isLoading = true) } }
            .onEach { list ->
                _uiState.update { it.copy(contacts = list, isLoading = false) }
            }
            .catch { e ->
                _uiState.update { it.copy(errorMessage = e.message, isLoading = false) }
            }
            .launchIn(viewModelScope)
    }

    fun syncContacts() {
        viewModelScope.launch {
            // This now "uses" the syncContacts function from the repository
            repository.syncContacts()
        }
    }
}
