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
        // Check if we can actually pop to prevent app closing unexpectedly
        if (navController.previousBackStackEntry != null) {
            navController.popBackStack()
        }
    }

    override fun navigateToAndClearStack(route: String) {
        navController.navigate(route) {
            popUpTo(0) { inclusive = true }
        }
    }


    fun handleDeepLink(intent: Intent) {
        val target = intent.getStringExtra("NAVIGATION_TARGET")
        target?.let { screenRoute ->
            // Logic:
            // If target is "notifications", it hits the AppNavHost route (Full Screen)
            // If target is "chat_list", it hits the main_shell route
            navController.navigate(screenRoute) {
                // Avoid multiple copies of the same screen when clicking notifications repeatedly
                launchSingleTop = true
                restoreState = true

                // If navigating to the main app from a deep link,
                // clear the login/signup stack
                popUpTo(navController.graph.startDestinationId) {
                    saveState = true
                }
            }
        }
    }
}