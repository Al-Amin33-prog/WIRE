package com.example.wire.feature.profile.presentation.screen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.wire.feature.profile.presentation.ProfileViewModel
import com.example.wire.feature.profile.presentation.event.ProfileUiEvent
import com.example.wire.feature.profile.presentation.screen.content.EditProfileContent



@Composable
fun EditProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    // THE SYMPHONY MOVE: Handle the heavy lifting (URI -> Bytes) in the Screen
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            try {
                context.contentResolver.openInputStream(it)?.use { inputStream ->
                    val bytes = inputStream.readBytes()
                    viewModel.onEvent(ProfileUiEvent.AvatarSelected(bytes))
                }
            } catch (e: Exception) {
                // You could emit a UI error state here if reading fails
                e.printStackTrace()
            }
        }
    }

    EditProfileContent(
        state = state,
        onEvent = viewModel::onEvent,
        onNavigateBack = onNavigateBack,
        onPickImage = { launcher.launch("image/*") }
    )
}