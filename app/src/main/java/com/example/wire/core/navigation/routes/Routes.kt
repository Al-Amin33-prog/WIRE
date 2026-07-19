package com.example.wire.core.navigation.routes

sealed class Routes(val route: String) {

    data object AuthGate : Routes("auth_gate")
      data object SecurityGate : Routes("security_gate")
    data object MainShell : Routes("main_shell")

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

    companion object {
        const val SIMULATOR_ID = "SIM_USER_001"
        const val SIMULATOR_NAME = "Wire Simulator (Bot)"
       const val SECURITY_GATE = "security_gate"
    }


}
