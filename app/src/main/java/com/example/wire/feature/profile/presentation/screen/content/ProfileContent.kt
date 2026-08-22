package com.example.wire.feature.profile.presentation.screen.content

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wire.feature.profile.presentation.component.ProfileHeader
import com.example.wire.feature.profile.presentation.component.ProfileInfoRow
import com.example.wire.feature.profile.presentation.event.ProfileUiEvent
import com.example.wire.feature.profile.presentation.state.ProfileUiState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileContent(
    state: ProfileUiState,
    onEvent: (ProfileUiEvent) -> Unit,
    onEditProfile: () -> Unit,
    onNavigateToSettings: () -> Unit
) {

    Scaffold (
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.statusBarsPadding(),
                title = {
                    Text("Profile",
                        style = MaterialTheme.typography.labelLarge,
                        letterSpacing = 2.sp)
                },
                actions = {
                    IconButton(
                        onClick = onNavigateToSettings
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },

                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                )
            )
        }
    ){ padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {

            item {

                ProfileHeader(
                    fullName = state.fullName,
                    username = state.username,
                    avatarUrl = state.avatarUrl,
                    isVerified = state.profile?.isVerified ?: false,
                    onAvatarClick = {}
                )

                Spacer(Modifier.height(24.dp))



                Spacer(Modifier.height(24.dp))

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onEditProfile
                ) {
                    Text("Edit Profile")
                }
            }
        }
    }
}