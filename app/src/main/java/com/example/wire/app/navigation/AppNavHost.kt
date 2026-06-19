package com.example.wire.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.wire.core.navigation.routes.Routes
import com.example.wire.feature.auth.presentation.screen.ForgotPasswordScreen
import com.example.wire.feature.auth.presentation.screen.LoginScreen
import com.example.wire.feature.auth.presentation.screen.SignUpScreen

@Composable
fun AppNavHost(navigatorImpl: NavigatorImpl) {
    val navController = rememberNavController()
    navigatorImpl.navController = navController

    NavHost(
        navController = navController,
        startDestination = Routes.Login.route
    ) {
        composable(Routes.Login.route) {
            LoginScreen(
                onNavigateToSignUp = { navController.navigate(Routes.SignUp.route) },
                onNavigateToForgotPassword = { navController.navigate("forgot_password") },
                onLoginSuccess = { navController.navigate(Routes.ChatList.route) }
            )
        }
        composable(Routes.SignUp.route) {
            SignUpScreen(
                onNavigateToLogin = { navController.popBackStack() },
                onSignUpSuccess = { navController.navigate(Routes.ChatList.route) }
            )
        }
        composable("forgot_password") {
            ForgotPasswordScreen(onNavigateBack = { navController.popBackStack() })
        }
    }
}