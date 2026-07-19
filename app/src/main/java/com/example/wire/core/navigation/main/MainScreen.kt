package com.example.wire.core.navigation.main


import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.wire.app.navigation.NavigatorImpl
import com.example.wire.core.navigation.routes.BottomNavItem
import com.example.wire.core.navigation.routes.Routes
import com.example.wire.feature.chat.presentation.screen.chat_list.ChatListScreen
import com.example.wire.feature.contacts.presentation.ContactSelectionScreen
import com.example.wire.feature.notifications.presentation.screen.NotificationsScreen
import com.example.wire.feature.payments.presentation.screen.PaymentSuccessScreen
import com.example.wire.feature.payments.presentation.screen.SendMoneyScreen
import com.example.wire.feature.profile.presentation.screen.EditProfileScreen
import com.example.wire.feature.profile.presentation.screen.ProfileScreen
import com.example.wire.feature.settings.presentation.SettingsScreen
import com.example.wire.feature.wallet.presentation.screen.WalletScreen

@Composable
fun MainScreen(navigatorImpl: NavigatorImpl) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val items = listOf(
        BottomNavItem.Chat,
        BottomNavItem.Send,
        BottomNavItem.Wallet,
        BottomNavItem.Profile,

    )

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.background,
                tonalElevation = 0.dp
            ) {
                items.forEach { item ->
                    // This logic ensures the 'Send' icon is highlighted when on the send route
                    val isSelected = currentDestination?.hierarchy?.any {
                        it.route?.startsWith(item.route) == true
                    } == true

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
                                modifier = if(item == BottomNavItem.Send){
                                    Modifier.rotate(-55f)
                                }else{
                                    Modifier
                                },
                                tint = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray
                            )
                        },
                        label = { Text(item.title, fontSize = 10.sp) },
                        colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Chat.route,
            modifier = Modifier.padding(padding)
        ) {
            // --- CHAT LIST ---
            composable(BottomNavItem.Chat.route) {
                ChatListScreen(
                    onChatClick = { chatId ->
                        navigatorImpl.navController?.navigate(Routes.Conversation.createRoute(chatId))
                    },
                    onNotificationClick = { navController.navigate("notifications") },
                    onFabClick = {
                        navController.navigate("contact_selection")
                    }
                )
            }

            composable("notifications") {
                NotificationsScreen(onBackClick = { navController.popBackStack() })
            }

            composable(BottomNavItem.Wallet.route) {
                WalletScreen(onBackClick = { navController.navigate(BottomNavItem.Chat.route) })
            }

            // --- THE UNIFIED SEND/REQUEST ROUTE ---
            // This replaces the two messy blocks you had. It handles everything.
            composable(
                route = "${BottomNavItem.Send.route}?recipientId={recipientId}&recipientName={recipientName}&mode={mode}&amount={amount}", // Added amount here
                arguments = listOf(
                    navArgument("recipientId") { defaultValue = "" },
                    navArgument("recipientName") { defaultValue = "Recipient" },
                    navArgument("mode") { defaultValue = "SEND" },
                    navArgument("amount") { defaultValue = "" } // THE FIX: Define amount as a navArgument
                )
            ) { backStackEntry ->
                val recipientId = backStackEntry.arguments?.getString("recipientId") ?: ""
                val recipientName = backStackEntry.arguments?.getString("recipientName") ?: "User"
                val mode = backStackEntry.arguments?.getString("mode") ?: "SEND"
                val amount = backStackEntry.arguments?.getString("amount") ?: ""

                SendMoneyScreen(
                    recipientId = recipientId,
                    recipientName = recipientName,
                    mode = mode,
                    amount = amount, // Now this will correctly carry the value from notifications/bubbles
                    onBackClick = { navController.popBackStack() },
                    onPaymentSuccess = { paidAmount, name ->
                        navController.navigate("payment_success/$paidAmount/$name") {
                            popUpTo(BottomNavItem.Send.route) { inclusive = true }
                        }
                    }
                )
            }

            // --- PAYMENT SUCCESS ---
            composable(
                route = "payment_success/{amount}/{name}",
                arguments = listOf(
                    navArgument("amount") { type = NavType.StringType },
                    navArgument("name") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val amount = backStackEntry.arguments?.getString("amount") ?: "0.00"
                val name = backStackEntry.arguments?.getString("name") ?: "Recipient"

                PaymentSuccessScreen(
                    amount = amount,
                    recipientName = name,
                    onDoneClick = { navController.navigate(BottomNavItem.Chat.route) }
                )
            }

            composable(BottomNavItem.Profile.route) {
                ProfileScreen(
                    onEditProfile = { navController.navigate("edit_profile") },
                    onNavigateToSettings = { navController.navigate("settings") }

                )
            }

            composable("edit_profile") {
                EditProfileScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            composable("settings") {
                SettingsScreen(
                    onBackClick = { navController.popBackStack() },
                    onLogoutSuccess = {
                        navigatorImpl.navController?.navigate(Routes.AuthGate.route) {
                            popUpTo(0)
                        }
                    }
                )
            }
            composable("contact_selection"){
                ContactSelectionScreen(
                    onBackClick = {navController.popBackStack()},
                    onContactSelected = { chatId ->
                        navigatorImpl.navController?.navigate(Routes.Conversation.createRoute(chatId))
                    }
                )
            }
            }
        }
    }
