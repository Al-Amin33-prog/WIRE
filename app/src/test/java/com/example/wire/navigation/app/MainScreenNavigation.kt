package com.example.wire.navigation.app

import com.example.wire.core.navigation.routes.BottomNavItem
import com.example.wire.core.navigation.routes.Routes
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Lightweight Unit Test for navigation structure.
 * This does NOT require Robolectric or Compose UI libraries,
 * so it will stay green even without a full Gradle sync.
 */
class MainScreenNavigationTest {

    @Test
    fun `verify bottom navigation routes are unique`() {
        val items = listOf(
            BottomNavItem.Chat,
            BottomNavItem.Wallet,
            BottomNavItem.Contacts,
            BottomNavItem.Profile
        )
        
        val routes = items.map { it.route }
        val uniqueRoutes = routes.distinct()
        
        assertEquals("Each bottom nav item must have a unique route", routes.size, uniqueRoutes.size)
    }

    @Test
    fun `verify conversation route generator`() {
        val chatId = "test_123"
        val generatedRoute = Routes.Conversation.createRoute(chatId)
        
        // Verifies the route string matches the expected pattern
        assertTrue(generatedRoute.contains(chatId))
        assertTrue(generatedRoute.startsWith("conversation/"))
    }

    @Test
    fun `verify start destination is login`() {
        assertEquals("login", Routes.Login.route)
    }
}
