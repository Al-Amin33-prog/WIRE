package com.example.wire.app.navigation
/*
import androidx.activity.compose.setContent
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider // Added for ApplicationProvider
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
    val composeTestRule = createComposeRule() // Fixed caret position

    private lateinit var navController: TestNavHostController
    private val navigator = NavigatorImpl()

    @Before
    fun setupAppNavHost() {
        composeTestRule.setContent {
            // Context is provided by Robolectric's ApplicationProvider
            navController = TestNavHostController(ApplicationProvider.getApplicationContext())
            navController.navigatorProvider.addNavigator(ComposeNavigator())

            // Wire the navigator to the test controller
            navigator.navController = navController

            AppNavHost(navigatorImpl = navigator)
        }
    }

    @Test
    fun appNavHost_verifyStartDestinationIsLogin() {
        // Assert that we start on the Login route
        assertEquals(Routes.Login.route, navController.currentBackStackEntry?.destination?.route)
    }

    @Test
    fun appNavHost_clickSignUp_navigatesToSignUp() {
        // Using substring = true to find "Create one" within the text row
        composeTestRule
            .onNodeWithText("Create one", substring = true)
            .performClick()

        // Verify the route changed to Sign Up
        assertEquals(Routes.SignUp.route, navController.currentBackStackEntry?.destination?.route)
    }
} */