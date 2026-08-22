package com.example.wire.feature.profile.presentation.screen.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.wire.feature.profile.presentation.component.ProfileAvatarPicker
import com.example.wire.feature.profile.presentation.event.ProfileUiEvent
import com.example.wire.feature.profile.presentation.state.ProfileUiState



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileContent(
    state: ProfileUiState,
    onEvent: (ProfileUiEvent) -> Unit,
    onNavigateBack: () -> Unit,
    onPickImage: () -> Unit // Added this to handle the image picker trigger
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Profile") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally // Center the picker
        ) {

            // FIX: Pass the state values and centering modifier
            ProfileAvatarPicker(
                imageUrl = state.avatarUrl,
                modifier = Modifier.padding(vertical = 8.dp),
                onAvatarClick = {
                    onPickImage()
                }
            )

            OutlinedTextField(
                value = state.fullName,
                onValueChange = { onEvent(ProfileUiEvent.FullNameChanged(it)) },
                label = { Text("Full Name") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.username,
                onValueChange = { onEvent(ProfileUiEvent.UsernameChanged(it)) },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.phoneNumber,
                onValueChange = { onEvent(ProfileUiEvent.PhoneChanged(it)) },
                label = { Text("Phone") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.bio,
                onValueChange = { onEvent(ProfileUiEvent.BioChanged(it)) },
                label = { Text("Bio") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.weight(1f))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    onEvent(ProfileUiEvent.SaveProfile)
                    // Navigation back is usually handled by a separate Effect
                    // in the screen when save is successful, but this works too
                    onNavigateBack()
                },
                enabled = !state.isSaving // Disable while saving
            ) {
                if (state.isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Save")
                }
            }
        }
    }
}