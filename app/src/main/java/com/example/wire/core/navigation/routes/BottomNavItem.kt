package com.example.wire.core.navigation.routes

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Chat : BottomNavItem(

        "chat_list",
        "Messages",
        Icons.Default.ChatBubble
    )
    object Wallet : BottomNavItem(

        "wallet",
        "Wallet",
        Icons.Default.AccountBalanceWallet
    )
    object Send : BottomNavItem(
        "send",
        "Send",
        Icons.AutoMirrored.Filled.Send
    )
    object Profile : BottomNavItem(
        "profile",
        "Profile",
        Icons.Default.Person
    )


}