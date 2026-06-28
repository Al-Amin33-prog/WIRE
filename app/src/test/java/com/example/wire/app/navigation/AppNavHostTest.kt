package com.example.wire.app.navigation

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import com.example.wire.core.navigation.routes.Routes
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class AppNavHostTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var navController: TestNavHostController
    private val navigator = NavigatorImpl()

    @Before
    fun setupAppNavHost() {
        // Initialize navController on the correct thread before setContent
        navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        
        composeTestRule.setContent {
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            navigator.navController = navController
            AppNavHost(navigatorImpl = navigator)
        }
    }

    @Test
    fun appNavHost_verifyStartDestinationIsLogin() {
        assertEquals(Routes.Login.route, navController.currentBackStackEntry?.destination?.route)
    }

    @Test
    fun appNavHost_clickSignUp_navigatesToSignUp() {
        composeTestRule
            .onNodeWithText("Create one", substring = true)
            .performClick()

        assertEquals(Routes.SignUp.route,
            navController.currentBackStackEntry?.destination?.route)
    }
}
