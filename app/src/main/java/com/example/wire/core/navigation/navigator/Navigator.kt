package com.example.wire.core.navigation.navigator

interface Navigator {
    fun navigateTo(route: String)
    fun navigateBack()
    fun navigateToAndClearStack(route: String)
}