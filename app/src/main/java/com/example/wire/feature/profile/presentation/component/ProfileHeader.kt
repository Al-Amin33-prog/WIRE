package com.example.wire.feature.profile.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ProfileHeader(
    fullName: String,
    username: String,
    avatarUrl: String?,
    isVerified: Boolean,
    modifier: Modifier = Modifier,
    onAvatarClick: () -> Unit
) {

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        ProfileAvatarPicker(
            imageUrl = avatarUrl,
            onAvatarClick = onAvatarClick
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = fullName,
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = "@$username",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        if (isVerified) {

            Spacer(Modifier.height(8.dp))

            AssistChip(
                onClick = { },
                label = {
                    Text("Verified")
                },
                leadingIcon = {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null
                    )
                }
            )
        }
    }
}