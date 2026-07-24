package com.example.wire.core.feature.security.presentation.screen


import android.util.Log
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.wire.core.feature.security.presentation.SecurityViewModel
import com.example.wire.core.feature.security.presentation.component.BiometricEnrollmentBottomSheet
import com.example.wire.core.feature.security.presentation.effect.SecurityUiEventEffect
import com.example.wire.core.feature.security.presentation.event.SecurityUiEvent
import com.example.wire.core.feature.security.presentation.state.SecurityStep
import com.example.wire.core.ui.util.LocalFragmentActivity
import com.example.wire.core.ui.util.WireBiometricManager


@Composable
fun SecurityGate(
    onSecurityComplete: () -> Unit,
    biometricManager: WireBiometricManager

){
    val viewModel: SecurityViewModel = hiltViewModel()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val activity = LocalFragmentActivity.current
    var showEnrollmentSheet by remember {
        mutableStateOf(true)
    }
    LaunchedEffect(Unit) {
        Log.d("startup","Security Gate started ")
    }

   LaunchedEffect(Unit) {
       viewModel.events.collect { effect->
           when(effect){
               SecurityUiEventEffect.LaunchedBiometricPrompt -> {
                   biometricManager.showBiometricPrompt(
                       activity = activity,
                       onSuccess = {
                           viewModel.onEvent(
                               SecurityUiEvent.BiometricAuthenticationSucceeded
                           )
                       },
                       onError = {
                           viewModel.onEvent(
                               SecurityUiEvent.BiometricFailed(it)
                           )

                       },
                       onFailed = {
                           viewModel.onEvent(
                               SecurityUiEvent.BiometricFailed("Authentication Failed")
                           )
                       }
                   )
               }
               SecurityUiEventEffect.NavigateToMainShell -> {
                   onSecurityComplete()
               }
               SecurityUiEventEffect.DismissBiometricSheet -> {

                   showEnrollmentSheet = false
               }

           }
       }
   }


    when(state.step){
        SecurityStep.SetPin -> {
            SetPinScreen(
                state = state,
                onEvent = viewModel::onEvent
            )
        }
        SecurityStep.EnrollBiometric -> {

            if (showEnrollmentSheet) {

                BiometricEnrollmentBottomSheet(

                    onEnroll = {
                        viewModel.onEvent(
                            SecurityUiEvent.EnableBiometricClicked
                        )
                    },

                    onDismiss = {
                        viewModel.onEvent(
                            SecurityUiEvent.DismissEnrollment
                        )
                    },
                    isLoading = state.biometricLoading
                )
            }
        }

        SecurityStep.Loading -> {
            CircularProgressIndicator()
        }
        SecurityStep.RequestBiometricAuthentication -> {

        }


        else -> {}
    }

}