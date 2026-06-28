package com.example.wire.feature.contacts.domain.model.usescase

import com.example.wire.feature.contacts.data.repository.ContactRepository
import javax.inject.Inject

class GetContactsUseCase @Inject constructor(
    private val repository: ContactRepository
) {
    operator fun invoke() = repository.getSyncedContacts()
}