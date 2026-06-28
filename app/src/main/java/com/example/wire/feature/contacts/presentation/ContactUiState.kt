package com.example.wire.feature.contacts.presentation



import com.example.wire.feature.contacts.domain.model.ContactUser

data class ContactUiState(
    val contacts: List<ContactUser> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)