package com.example.wire.core.navigation.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.wire.app.navigation.NavigatorImpl
import com.example.wire.core.navigation.routes.BottomNavItem
import com.example.wire.core.navigation.routes.Routes // Added missing import
import com.example.wire.feature.chat.presentation.screen.chat_list.ChatListScreen // Added missing import
import com.example.wire.feature.notifications.presentation.screen.NotificationsScreen // Added missing import

@Composable
fun MainScreen(navigatorImpl: NavigatorImpl) {
    // This is the controller for the INNER navigation (Bottom Bar tabs)
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val items = listOf(
        BottomNavItem.Chat,
        BottomNavItem.Wallet,
        BottomNavItem.Contacts,
        BottomNavItem.Notification,
        BottomNavItem.Profile
    )

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.background,
                tonalElevation = 8.dp
            ) {
                items.forEach { item ->
                    val isSelected = currentDestination?.hierarchy?.any { it.route == item.route } == true

                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.title,
                                tint = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray
                            )
                        },
                        label = { Text(item.title, fontSize = 10.sp) }
                    )
                }
            }
        }
    ) { padding ->
        // FIX 1: Changed 'innerNavController' to 'navController' to match your declaration
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Chat.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(BottomNavItem.Chat.route) {
                ChatListScreen(onChatClick = { chatId ->
                    // FIX 2: Use the navigatorImpl (OUTER navigator) to go to the full-screen Conversation
                    navigatorImpl.navController?.navigate(Routes.Conversation.createRoute(chatId))
                })
            }

            composable(BottomNavItem.Notification.route) {
                // FIX 3: Parameter names must match your NotificationsScreen declaration
                NotificationsScreen(
                    onBackClick = {
                        // If they press back in activity, take them back to the chat tab
                        navController.navigate(BottomNavItem.Chat.route)
                    }
                )
            }


            composable(BottomNavItem.Wallet.route) {
                Box(Modifier.padding(16.dp)) { Text("Wallet Coming Soon") }
            }
            composable(BottomNavItem.Contacts.route) {
                Box(Modifier.padding(16.dp)) { Text("Contacts Coming Soon") }
            }
            composable(BottomNavItem.Profile.route) {
                Box(Modifier.padding(16.dp)) { Text("Profile Coming Soon") }
            }
        }
    }
}