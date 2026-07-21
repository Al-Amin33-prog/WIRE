package com.example.wire.core.feature.security.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.example.wire.core.feature.security.presentation.event.SecurityUiEvent
import com.example.wire.core.feature.security.presentation.state.SecurityUiState


@Composable
fun SetPinScreen(
    state: SecurityUiState,
    onEvent: (SecurityUiEvent) -> Unit
){
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets.safeDrawing,
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Text(
                "Create your security PIN",
                style = MaterialTheme.typography.headlineSmall
            )
            Text("This PIN will be required before sending money and authorizing sensitive actions")
            OutlinedTextField(
                value = state.pin,
                onValueChange = { value ->
                    if(value.length <= 4 && value.all { it.isDigit() }){
                        onEvent(SecurityUiEvent.PinChanged(value))
                    }
                },
                label = {
                    Text("PIN")
                },
                keyboardOptions =
                    KeyboardOptions(
                        keyboardType = KeyboardType.NumberPassword)
            )
            Button(
                onClick = {
                    onEvent(
                        SecurityUiEvent.CreatePinClicked
                    )
                },
                enabled = state.pin.length == 4
            ) {
                Text("Continue")
            }
        }
    }

}