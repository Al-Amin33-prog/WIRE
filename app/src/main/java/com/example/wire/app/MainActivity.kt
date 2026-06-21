package com.example.wire.app

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.fragment.app.FragmentActivity
import com.example.wire.app.navigation.AppNavHost
import com.example.wire.app.navigation.NavigatorImpl
import com.example.wire.core.ui.util.LocalFragmentActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    @Inject
    lateinit var navigatorImpl: NavigatorImpl
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Provide the activity context for Biometrics/Permissions
            CompositionLocalProvider(LocalFragmentActivity provides this) {
                AppNavHost(
                    navigatorImpl = navigatorImpl
                )

            }
        }
    }
}