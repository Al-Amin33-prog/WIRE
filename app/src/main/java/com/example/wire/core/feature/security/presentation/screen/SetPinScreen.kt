package com.example.wire.core.feature.security.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.wire.core.feature.security.presentation.event.SecurityUiEvent
import com.example.wire.core.feature.security.presentation.state.SecurityUiState

@Composable
fun SetPinScreen(
    state: SecurityUiState,
    onEvent: (SecurityUiEvent) -> Unit
){
    Column {
        Text(
            "Create your security PIN",
            style = MaterialTheme.typography.headlineSmall
        )
        OutlinedTextField(
            value = state.pin,
            onValueChange = {
                onEvent(
                    SecurityUiEvent.PinChanged(it)
                )
            },
            label = {
                Text("PIN")
            }
        )
        Button(
            onClick = {
                SecurityUiEvent.CreatePinClicked
            }
        ) {
            Text("Continue")
        }
    }
}