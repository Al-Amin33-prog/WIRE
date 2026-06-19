package com.example.wire.core.navigation.routes

sealed class Routes(val route: String) {
    object Login : Routes("login")
    object SignUp : Routes("signup")
    object ChatList : Routes("chat_list")
    object Conversation : Routes("conversation/{chatId}") {
        fun createRoute(chatId: String) = "conversation/$chatId"
    }
    object Wallet : Routes("wallet")
    object SendMoney : Routes("send_money")
    object Notifications : Routes("notifications")
    object Profile : Routes("profile")
    object Settings : Routes("settings")
    object ForgotPassword : Routes("forgot_password")
}