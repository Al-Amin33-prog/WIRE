package com.example.wire.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.wire.core.navigation.routes.Routes
import com.example.wire.feature.auth.presentation.screen.ForgotPasswordScreen
import com.example.wire.feature.auth.presentation.screen.LoginScreen
import com.example.wire.feature.auth.presentation.screen.SignUpScreen
import com.example.wire.feature.chat.presentation.screen.chat_list.ChatListScreen
import com.example.wire.feature.chat.presentation.screen.conversation.ConversationScreen


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
        composable(Routes.ChatList.route) {

         ChatListScreen(
             onChatClick = {chatId ->
                 navController.navigate(Routes.Conversation.createRoute(chatId))
             }
         )
        }
        composable(
            route = Routes.Conversation.route,    arguments = listOf(
                navArgument("chatId") {
                    type = NavType.StringType }
            )
        ) { backStackEntry ->
            val chatId = backStackEntry.arguments?.getString("chatId") ?: ""
            ConversationScreen(
                chatId = chatId,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}