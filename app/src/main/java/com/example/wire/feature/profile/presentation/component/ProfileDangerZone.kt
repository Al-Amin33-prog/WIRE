package com.example.wire.feature.profile.presentation.component



import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProfileDangerZone(
    onSignOutClicked: () -> Unit,
    onDeleteAccountClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 22.dp)
            .border(
                width = 1.5.dp,
                color = Color(0xFFD94040).copy(alpha = 0.18f),
                shape = RoundedCornerShape(20.dp)
            ),
        shape = RoundedCornerShape(20.dp),
        color = Color(0xFFD94040).copy(alpha = 0.04f)
    ) {
        Column {
            // Sign Out
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSignOutClicked() }
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Logout,
                    contentDescription = "Sign out",
                    tint = Color(0xFFD94040),
                    modifier = Modifier.size(18.dp)
                )
                Column {
                    Text(
                        text = "Sign Out",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFD94040)
                    )
                    Text(
                        text = "You will need to sign in again",
                        fontSize = 12.sp,
                        color = Color(0xFFD94040).copy(alpha = 0.6f),
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }

            Divider(
                color = Color(0xFFD94040).copy(alpha = 0.12f),
                thickness = 1.dp
            )

            // Delete Account
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onDeleteAccountClicked() }
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = "Delete account",
                    tint = Color(0xFFD94040),
                    modifier = Modifier.size(18.dp)
                )
                Column {
                    Text(
                        text = "Delete Account",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFD94040)
                    )
                    Text(
                        text = "This action is permanent and irreversible",
                        fontSize = 12.sp,
                        color = Color(0xFFD94040).copy(alpha = 0.6f),
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }
    }
}