package com.example.wire.feature.profile.presentation.component

data class ProfileItem(
    val titleRes: Int,
    val subtitleRes: Int,
    val icon: Int,
    val route: String? = null,
    val isDestructive: Boolean = false
)