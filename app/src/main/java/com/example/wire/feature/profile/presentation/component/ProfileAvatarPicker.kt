package com.example.wire.feature.profile.presentation.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun ProfileAvatarPicker(
    imageUrl: String?,
    modifier: Modifier = Modifier,
    onAvatarClick: () -> Unit
) {

    Box(
        modifier = modifier,
        contentAlignment = Alignment.BottomEnd
    ) {

        AsyncImage(
            model = imageUrl,
            contentDescription = "Profile Avatar",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .border(
                    2.dp,
                    MaterialTheme.colorScheme.primary,
                    CircleShape
                ),
            contentScale = ContentScale.Crop
        )

        FloatingActionButton(
            onClick = onAvatarClick,
            modifier = Modifier.size(38.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit Avatar"
            )
        }
    }
}


