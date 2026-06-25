package com.example.wire.app

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.core.util.Consumer
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
            LaunchedEffect(intent) {
                intent?.let { navigatorImpl.handleDeepLink(it) }
            }
            DisposableEffect(Unit) {
                val listener = Consumer<Intent>{intent ->
                    navigatorImpl.handleDeepLink(intent)
                }
                addOnNewIntentListener(listener)
                onDispose {
                    removeOnNewIntentListener(listener)
                }
            }
            // Provide the activity context for Biometrics/Permissions
            CompositionLocalProvider(LocalFragmentActivity provides this) {
                AppNavHost(
                    navigatorImpl = navigatorImpl
                )


            }
        }
    }
}