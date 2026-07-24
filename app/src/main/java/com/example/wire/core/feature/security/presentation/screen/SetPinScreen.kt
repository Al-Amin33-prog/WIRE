package com.example.wire.core.feature.security.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.wire.core.feature.security.presentation.component.NumericKeypad
import com.example.wire.core.feature.security.presentation.component.PinIndicator
import com.example.wire.core.feature.security.presentation.event.SecurityUiEvent
import com.example.wire.core.feature.security.presentation.state.SecurityStep
import com.example.wire.core.feature.security.presentation.state.SecurityUiState


@Composable
fun SetPinScreen(
    state: SecurityUiState,
    onEvent: (SecurityUiEvent) -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets.safeDrawing
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(Modifier.height(48.dp))

            Text(
                text =
                    if (state.step == SecurityStep.ConfirmPin)
                        "Confirm your PIN"
                    else
                        "Create your PIN",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text =
                    if (state.step == SecurityStep.ConfirmPin)
                        "Re-enter the same PIN to continue."
                    else
                        "Use this PIN to authorize payments and secure your account."
            )

            Spacer(Modifier.height(40.dp))

            PinIndicator(
                pin =
                    if (state.step == SecurityStep.ConfirmPin)
                        state.confirmPin
                    else
                        state.pin,
                pinLength = 4
            )
            state.pinError?.let { error ->
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(Modifier.height(48.dp))

            NumericKeypad(

                onNumberClick = { number ->

                    val currentPin =
                        if (state.step == SecurityStep.ConfirmPin)
                            state.confirmPin
                        else
                            state.pin

                    if (currentPin.length < 4) {

                        val newPin = currentPin + number

                        onEvent(
                            if (state.step == SecurityStep.ConfirmPin)
                                SecurityUiEvent.ConfirmPinChanged(newPin)
                            else
                                SecurityUiEvent.PinChanged(newPin)
                        )

                        if (newPin.length == 4) {
                            onEvent(SecurityUiEvent.CreatePinClicked)
                        }
                    }
                },

                onDelete = {

                    val currentPin =
                        if (state.step == SecurityStep.ConfirmPin)
                            state.confirmPin
                        else
                            state.pin

                    if (currentPin.isNotEmpty()) {

                        val updated = currentPin.dropLast(1)

                        onEvent(
                            if (state.step == SecurityStep.ConfirmPin)
                                SecurityUiEvent.ConfirmPinChanged(updated)
                            else
                                SecurityUiEvent.PinChanged(updated)
                        )
                    }
                }
            )
        }
    }
}