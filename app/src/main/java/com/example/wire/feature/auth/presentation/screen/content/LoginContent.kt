package com.example.wire.feature.auth.presentation.screen.content


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wire.R
import com.example.wire.core.ui.theme.SurfaceDark
import com.example.wire.feature.auth.presentation.event.AuthUiEvent
import com.example.wire.feature.auth.presentation.state.AuthUiState

@Composable
fun LoginContent(
    uiState: AuthUiState,
    onEvent: (AuthUiEvent) -> Unit,
    onNavigateToSignUp: () -> Unit,
    onNavigateToForgotPassword: () -> Unit
) {
    var passwordVisible by remember{ mutableStateOf(false
    ) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 28.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "SEND.\nCHAT.\nWIRE.",
            fontSize = 42.sp,
            lineHeight = 48.sp,
            fontWeight = FontWeight.Black,
            color = Color.White,
            letterSpacing = 2.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "The messaging app where money moves as fast as conversation.",
            fontSize = 16.sp,
            color = Color.Gray,
            modifier = Modifier.fillMaxWidth(0.8f)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "EMAIL",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = uiState.email,
            onValueChange = { onEvent(AuthUiEvent.EmailChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor =  Color.Unspecified,
                unfocusedContainerColor = Color.Unspecified,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            placeholder = { Text("alex@gmail.com", color = Color.DarkGray) }
        )

        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "PASSWORD",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = uiState.password,
            onValueChange = { onEvent(AuthUiEvent.PasswordChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            visualTransformation = if (passwordVisible) androidx.compose.ui.text.input.VisualTransformation.None
            else androidx.compose.ui.text.input.PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Visibility
                        else Icons.Default.VisibilityOff,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                }
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = SurfaceDark,
                unfocusedContainerColor = SurfaceDark,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = stringResource(R.string.login_forgot_password),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .align(Alignment.End)
                .clickable { onNavigateToForgotPassword() }
        )

        Spacer(modifier = Modifier.height(24.dp))

        uiState.errorMessage?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }

        Button(
            onClick = { onEvent(AuthUiEvent.LoginClicked) },
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp))
            } else {
                Text(stringResource(R.string.login_button_text))
            }
        }

        if (uiState.isBiometricAvailable && uiState.isBiometricEnabled) {
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = { onEvent(AuthUiEvent.BiometricLoginClicked) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Fingerprint,
                    contentDescription = stringResource(R.string.login_biometric_button),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(R.string.login_biometric_button))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.login_no_account),
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
            Text(
                text = stringResource(R.string.login_create_one),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { onNavigateToSignUp() }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HorizontalDivider(modifier = Modifier.weight(1f))
            Text(
                text = "  ${stringResource(R.string.login_or_continue_with)}  ",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
            )
            HorizontalDivider(modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painterResource(R.drawable.google__g__logo),
                contentDescription = "Google_logo",
                modifier = Modifier
                    .clickable {
                        onEvent(AuthUiEvent.GoogleSignInClicked)
                    }
                    .size(16.dp)

            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.login_google_button),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
        }



    }
}
