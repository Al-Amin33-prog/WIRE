package com.example.wire.core.navigation.routes

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Notifications
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
    object Contacts : BottomNavItem(
        "contacts",
        "Contacts",
        Icons.Default.Contacts
    )
    object Profile : BottomNavItem(
        "profile",
        "Profile",
        Icons.Default.Person
    )
    object Notification: BottomNavItem(
       "Alerts",
        "Alerts",
        Icons.Default.Notifications
    )

}