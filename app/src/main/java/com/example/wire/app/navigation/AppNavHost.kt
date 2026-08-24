package com.example.wire.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.wire.core.feature.security.presentation.screen.SecurityGate
import com.example.wire.core.navigation.main.MainScreen
import com.example.wire.core.navigation.routes.Routes
import com.example.wire.core.ui.util.WireBiometricManager
import com.example.wire.feature.auth.presentation.authgate.AuthGate
import com.example.wire.feature.auth.presentation.screen.ForgotPasswordScreen
import com.example.wire.feature.auth.presentation.screen.LoginScreen
import com.example.wire.feature.auth.presentation.screen.SignUpScreen
import com.example.wire.feature.chat.presentation.screen.conversation.ConversationScreen
import com.example.wire.feature.contacts.presentation.ContactSelectionScreen

@Composable
fun AppNavHost(
    navigatorImpl: NavigatorImpl,
    startDestination: String = Routes.AuthGate.route,
    biometricManager: WireBiometricManager
) {
    val navController = navigatorImpl.navController ?: return

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        composable(Routes.AuthGate.route) {
            AuthGate(
                onAuthenticated = {
                    navController.navigate(Routes.SecurityGate.route) {
                        popUpTo(Routes.AuthGate.route) { inclusive = true }
                    }
                },
                onUnauthenticated = {
                    navController.navigate(Routes.Login.route) {
                        popUpTo(Routes.AuthGate.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.SecurityGate.route) {
            SecurityGate(
                onSecurityComplete = {
                    navController.navigate(Routes.MainShell.route) {
                        popUpTo(Routes.SecurityGate.route) { inclusive = true }
                    }
                },
                biometricManager = biometricManager
            )
        }

        // --- AUTH GROUP ---
        composable(Routes.Login.route) {
            LoginScreen(
                onNavigateToSignUp = { navController.navigate(Routes.SignUp.route) },
                onNavigateToForgotPassword = {
                    navController.navigate("forgot_password")
                                             },
                onLoginSuccess = {
                    // FIXED: Move forward to SecurityGate after login
                    navController.navigate(Routes.SecurityGate.route) {
                        popUpTo(Routes.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.SignUp.route) {
            SignUpScreen(
                onNavigateToLogin = { navController.popBackStack() },
                onSignUpSuccess = {
                    // FIXED: Move forward to SecurityGate after signup
                    navController.navigate(Routes.SecurityGate.route) {
                        popUpTo(Routes.SignUp.route) { inclusive = true }
                    }
                }
            )
        }

        composable("forgot_password") {
            ForgotPasswordScreen(onNavigateBack = { navController.popBackStack() })
        }

        // --- MAIN APP SHELL ---
        composable(Routes.MainShell.route) {
            MainScreen(navigatorImpl = navigatorImpl)
        }

        // --- FULL SCREEN ROUTES ---
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
                //onLongClick = { /* Handle delete/edit */ },
                navController = navController
            )
        }

        composable("contact_selection") {
            ContactSelectionScreen(
                onContactSelected = { userId ->
                    navController.navigate(Routes.Conversation.createRoute(userId)) {
                        popUpTo("contact_selection") { inclusive = true }
                    }
                },
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
