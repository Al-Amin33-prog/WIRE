package com.example.wire.app.navigation

import android.content.Intent
import androidx.navigation.NavHostController
import com.example.wire.core.navigation.navigator.Navigator
import javax.inject.Inject

class NavigatorImpl @Inject constructor() : Navigator {
    var navController: NavHostController? = null // Changed from lateinit to nullable

    override fun navigateTo(route: String) {
        navController?.navigate(route)
    }

    override fun navigateBack() {
        if (navController?.previousBackStackEntry != null) {
            navController?.popBackStack()
        }
    }

    override fun navigateToAndClearStack(route: String) {
        navController?.navigate(route) {
            popUpTo(0) { inclusive = true }
        }
    }

    fun handleDeepLink(intent: Intent) {
        val target = intent.getStringExtra("NAVIGATION_TARGET")
        target?.let { screenRoute ->
            navController?.navigate(screenRoute) {
                launchSingleTop = true
                restoreState = true
                popUpTo(navController?.graph?.startDestinationId ?: return@let) {
                    saveState = true
                }
            }
        }
    }
}
