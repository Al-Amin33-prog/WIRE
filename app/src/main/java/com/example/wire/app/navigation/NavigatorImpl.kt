package com.example.wire.app.navigation

import androidx.navigation.NavHostController
import com.example.wire.core.navigation.navigator.Navigator
import javax.inject.Inject

class NavigatorImpl @Inject constructor() : Navigator {
    lateinit var navController: NavHostController

    override fun navigateTo(route: String) {
        navController.navigate(route)
    }

    override fun navigateBack() {
        navController.popBackStack()
    }

    override fun navigateToAndClearStack(route: String) {
        navController.navigate(route) {
            popUpTo(0) { inclusive = true }
        }
    }
}