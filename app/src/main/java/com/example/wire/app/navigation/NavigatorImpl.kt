package com.example.wire.app.navigation

import android.content.Intent
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
    // Inside NavigatorImpl.kt

    fun handleDeepLink(intent: Intent) {
        val target = intent.getStringExtra("NAVIGATION_TARGET")
        target?.let { screenRoute ->
            // Use your existing navigation logic to move to the screen
            // e.g., navigateTo(screenRoute)
            navController.navigate(screenRoute) {
                launchSingleTop = true
            }
        }
    }
}