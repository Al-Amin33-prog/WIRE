package com.example.wire.feature.contacts.domain.model



data class ContactUser(
    val id: String,
    val name: String,
    val lastMessage: String,
    val avatarColor: Int,
    val isOnline: Boolean = false
)
