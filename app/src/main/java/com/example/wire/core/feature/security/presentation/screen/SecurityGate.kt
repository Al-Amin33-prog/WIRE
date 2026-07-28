package com.example.wire.core.feature.security.presentation.screen


import android.util.Log
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.wire.core.feature.security.presentation.SecurityViewModel
import com.example.wire.core.feature.security.presentation.component.BiometricEnrollmentBottomSheet
import com.example.wire.core.feature.security.presentation.component.PinVerificationBottomSheet
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
               SecurityUiEventEffect.VerificationSucceeded -> {

               }


           }
       }
   }


    when(state.step){
        SecurityStep.SetPin,
             SecurityStep.ConfirmPin -> {
            SetPinScreen(
                state = state,
                onEvent = viewModel::onEvent
            )
        }

        SecurityStep.EnrollBiometric -> {

            if (state.showBiometricSheet) {

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
        SecurityStep.VerifyPin ->{
            PinVerificationBottomSheet(
                pin = state.verificationPin,
                error = state.verificationError,
                onNumberClick = {number ->
                    val newPin = state.verificationPin + number
                    viewModel.onEvent(
                        SecurityUiEvent.VerificationPinChanged(newPin)
                    )
                    if (newPin.length == 4){
                        viewModel.onEvent(
                            SecurityUiEvent.VerifyPinClicked
                        )
                    }
                },
                onDelete = {
                    if (state.verificationPin.isNotEmpty()){
                        viewModel.onEvent(
                            SecurityUiEvent.VerificationPinChanged(
                                state.verificationPin.dropLast(1)
                            )
                        )
                    }
                },
                onDismiss = {
                    viewModel.onEvent(
                        SecurityUiEvent.CancelVerification
                    )
                }

            )
        }


        SecurityStep.Loading -> {
            CircularProgressIndicator()
        }
        SecurityStep.RequestBiometricAuthentication -> {

        }
        SecurityStep.Completed -> {

        }



    }

}