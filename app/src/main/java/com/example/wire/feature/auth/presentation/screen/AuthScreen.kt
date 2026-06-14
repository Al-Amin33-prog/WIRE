package com.example.wire.feature.auth.presentation.screen

import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun AuthScreen(){
    Text("Shaba",modifier = Modifier.height(13.dp))

}
@Preview
@Composable
fun AuthScreenPreview(){
    AuthScreen()
}