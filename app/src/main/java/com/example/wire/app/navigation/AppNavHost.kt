package com.example.wire.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

import com.example.wire.core.navigation.main.MainScreen
import com.example.wire.core.navigation.routes.Routes
import com.example.wire.feature.auth.presentation.screen.ForgotPasswordScreen
import com.example.wire.feature.auth.presentation.screen.LoginScreen
import com.example.wire.feature.auth.presentation.screen.SignUpScreen
import com.example.wire.feature.chat.presentation.screen.conversation.ConversationScreen

@Composable
fun AppNavHost(
    navigatorImpl: NavigatorImpl,
    startDestination: String = Routes.Login.route
) {
    val navController = navigatorImpl.navController ?: return

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // --- AUTH GROUP ---
        composable(Routes.Login.route) {
            LoginScreen(
                onNavigateToSignUp = { navController.navigate(Routes.SignUp.route) },
                onNavigateToForgotPassword = { navController.navigate("forgot_password") },
                // SUCCESS: Navigate to MainScreen (which contains the Bottom Bar)
                onLoginSuccess = {
                    navController.navigate("main_shell") {
                        popUpTo(Routes.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.SignUp.route) {
            SignUpScreen(
                onNavigateToLogin = { navController.popBackStack() },
                onSignUpSuccess = {
                    navController.navigate("main_shell") {
                        popUpTo(Routes.SignUp.route) { inclusive = true }
                    }
                }
            )
        }

        composable("forgot_password") {
            ForgotPasswordScreen(onNavigateBack = { navController.popBackStack() })
        }

        // --- MAIN APP SHELL (Bottom Bar Lives Here) ---
        composable("main_shell") {
            MainScreen(navigatorImpl = navigatorImpl)
        }

        // --- FULL SCREEN ROUTES (No Bottom Bar) ---
        composable(
            route = Routes.Conversation.route,
            arguments = listOf(
                navArgument("chatId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val chatId = backStackEntry.arguments?.getString("chatId") ?: ""
            ConversationScreen(
                chatId = chatId,
                onBackClick = { navController.popBackStack() },
                onLongClick = { /* Handle delete/edit */ }
            )
        }
    }
}