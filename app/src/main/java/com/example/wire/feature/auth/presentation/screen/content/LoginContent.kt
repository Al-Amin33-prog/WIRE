package com.example.wire.feature.auth.presentation.screen.content


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wire.R
import com.example.wire.feature.auth.presentation.event.AuthUiEvent
import com.example.wire.feature.auth.presentation.state.AuthUiState

@Composable
fun LoginContent(
    uiState: AuthUiState,
    onEvent: (AuthUiEvent) -> Unit,
    onNavigateToSignUp: () -> Unit,
    onNavigateToForgotPassword: () -> Unit
) {
    var passwordVisible by remember { mutableStateOf(false) }


    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            // This prevents "Overlap" - the user can scroll if the screen is too short
            .verticalScroll(scrollState)
            .padding(horizontal = 28.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        // --- HEADER ---
        Text(
            text = stringResource(R.string.app_tagline),
            fontSize = 42.sp,
            lineHeight = 46.sp, // Slightly tighter line height
            fontWeight = FontWeight.Black,
            color = MaterialTheme.colorScheme.onBackground,
            letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.the_app_where_money_),
            fontSize = 15.sp, // Slightly smaller for better fit
            color = Color.Gray,
            modifier = Modifier.fillMaxWidth(0.9f)
        )

        Spacer(modifier = Modifier.height(24.dp)) // Reduced from 32

        // --- EMAIL FIELD ---
        Text(
            text = stringResource(R.string.email),
            style = MaterialTheme.typography.labelLarge,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = uiState.email,
            onValueChange = { onEvent(AuthUiEvent.EmailChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                unfocusedIndicatorColor = Color.Gray.copy(alpha = 0.5f),
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface
            ),
            placeholder = {
                Text(stringResource(R.string.user_email_com), color = Color.Gray)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // --- PASSWORD FIELD ---
        Text(
            text = stringResource(R.string.password),
            style = MaterialTheme.typography.labelLarge,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = uiState.password,
            onValueChange = { onEvent(AuthUiEvent.PasswordChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None
            else PasswordVisualTransformation(),
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
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                unfocusedIndicatorColor = Color.Gray.copy(alpha = 0.5f),
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface
            ),
            placeholder = {
                Text(stringResource(R.string.asteric))
            }
        )

        // Forgot Password link
        Text(
            text = stringResource(R.string.login_forgot_password),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .padding(top = 8.dp)
                .align(Alignment.End)
                .clickable { onNavigateToForgotPassword() }
        )

        Spacer(modifier = Modifier.height(32.dp))

        // --- ERROR MESSAGE ---
        uiState.errorMessage?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        // --- LOGIN BUTTON ---
        Button(
            onClick = { onEvent(AuthUiEvent.LoginClicked) },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            enabled = !uiState.isLoading,
            shape = RoundedCornerShape(12.dp)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White)
            } else {
                Text(stringResource(R.string.login_button_text), fontWeight = FontWeight.Bold)
            }
        }

        // --- BIOMETRIC OPTION ---
        if (uiState.isBiometricAvailable && uiState.isBiometricEnabled) {
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedButton(
                onClick = { onEvent(AuthUiEvent.BiometricLoginClicked) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Fingerprint, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(R.string.login_biometric_button))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- FOOTER (Sign Up / Google) ---
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row {
                Text(stringResource(R.string.login_no_account), color = Color.Gray)
                Spacer(Modifier.width(4.dp))
                Text(
                    text = stringResource(R.string.login_create_one),
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onNavigateToSignUp() }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Google Login
            OutlinedButton(
                onClick = { onEvent(AuthUiEvent.GoogleSignInClicked) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Image(
                    painterResource(R.drawable.google__g__logo),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(stringResource(R.string.login_google_button), color = MaterialTheme.colorScheme.onBackground)
            }
        }
    }
}
