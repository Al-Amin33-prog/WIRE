package com.example.wire.navigation.app

import androidx.navigation.NavHostController
import com.example.wire.app.navigation.NavigatorImpl

import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

/**
 * This is a lightweight Unit Test for navigation logic.
 * It does not require Robolectric or Compose UI Test libraries, 
 * so it will work without a Gradle sync.
 */
class NavigatorTest {

    private val navController = mockk<NavHostController>(relaxed = true)
    private lateinit var navigator: NavigatorImpl

    @Before
    fun setup() {
        navigator = NavigatorImpl()
        navigator.navController = navController
    }

    @Test
    fun `navigateTo calls navController navigate`() {
        val route = "test_route"
        navigator.navigateTo(route)
        
        verify { navController.navigate(route) }
    }

    @Test
    fun `navigateBack calls navController popBackStack`() {
        navigator.navigateBack()

        verify { navController.popBackStack() }
    }
}
