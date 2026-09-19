package com.example.wire.feature.profile.presentation.screen.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wire.feature.profile.domain.model.Profile
import com.example.wire.feature.profile.presentation.component.NovaPayIdCard
import com.example.wire.feature.profile.presentation.component.ProfileDangerZone
import com.example.wire.feature.profile.presentation.component.ProfileHeader
import com.example.wire.feature.profile.presentation.component.ProfileInfoRow
import com.example.wire.feature.profile.presentation.component.ProfileSectionCard
import com.example.wire.feature.profile.presentation.component.ProfileSectionDivider
import com.example.wire.feature.profile.presentation.component.ProfileStatsCard
import com.example.wire.feature.profile.presentation.component.ProfileToggleRow
import com.example.wire.feature.profile.presentation.event.ProfileUiEvent
import com.example.wire.feature.profile.presentation.state.ProfileUiState

@Composable
fun ProfileContent(
    uiState: ProfileUiState,
    onEvent: (ProfileUiEvent) -> Unit
) {
    val scrollState = rememberScrollState()
    val profile = uiState.profile

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F5F0))
    ) {
        if (profile == null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF0B1A3E))
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(bottom = 90.dp)
            ) {
                // Header — cover + avatar + edit button
                ProfileHeader(
                    profile = profile,
                    isEditing = uiState.isEditing,
                    onEditClicked = { onEvent(ProfileUiEvent.EditClicked) },
                    onSaveClicked = { onEvent(ProfileUiEvent.SaveProfileClicked) },
                    onCancelClicked = { onEvent(ProfileUiEvent.CancelEditClicked) }
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Name and bio block
                Column(
                    modifier = Modifier.padding(horizontal = 22.dp)
                ) {
                    if (uiState.isEditing) {
                        OutlinedTextField(
                            value = uiState.editDisplayName,
                            onValueChange = { onEvent(ProfileUiEvent.DisplayNameChanged(it)) },
                            label = { Text("Display Name") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFFC9952A),
                                focusedLabelColor = Color(0xFFC9952A),
                                cursorColor = Color(0xFF0B1A3E)
                            )
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedTextField(
                            value = uiState.editBio,
                            onValueChange = { onEvent(ProfileUiEvent.BioChanged(it)) },
                            label = { Text("Bio") },
                            modifier = Modifier.fillMaxWidth(),
                            maxLines = 3,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFFC9952A),
                                focusedLabelColor = Color(0xFFC9952A),
                                cursorColor = Color(0xFF0B1A3E)
                            )
                        )
                    } else {
                        Text(
                            text = profile.displayName,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0B1A3E)
                        )
                        Text(
                            text = "@${profile.displayName.lowercase().replace(" ", ".")} · NovaPay",
                            fontSize = 13.sp,
                            color = Color(0xFF5A6480),
                            modifier = Modifier.padding(top = 3.dp)
                        )
                        if (!profile.bio.isNullOrBlank()) {
                            Text(
                                text = profile.bio,
                                fontSize = 13.sp,
                                color = Color(0xFF5A6480),
                                lineHeight = 20.sp,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Stats card
                ProfileStatsCard(profile = profile)

                Spacer(modifier = Modifier.height(16.dp))

                // NovaPay ID card
                NovaPayIdCard(
                    novaPayId = profile.wireId,
                    onShareClicked = { /* share logic */ }
                )

                // Account section
                SectionLabel(text = "Account")

                ProfileSectionCard {
                    ProfileInfoRow(
                        title = "Personal Information",
                        subtitle = "Name, email, phone number",
                        icon = Icons.Outlined.Person,
                        iconTint = Color(0xFF0B1A3E),
                        iconBackground = Color(0xFF0B1A3E).copy(alpha = 0.07f),
                        onClick = { }
                    )
                    ProfileSectionDivider()
                    ProfileInfoRow(
                        title = "Payment Methods",
                        subtitle = "Cards, bank accounts",
                        icon = Icons.Outlined.CreditCard,
                        iconTint = Color(0xFFC9952A),
                        iconBackground = Color(0xFFC9952A).copy(alpha = 0.1f),
                        onClick = { },
                        trailingContent = {
                            Surface(
                                shape = MaterialTheme.shapes.small,
                                color = Color(0xFF0B1A3E)
                            ) {
                                Text(
                                    text = "2 linked",
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    )
                    ProfileSectionDivider()
                    ProfileInfoRow(
                        title = "KYC Verification",
                        subtitle = if (profile.isKycVerified) "Identity fully verified" else "Tap to verify your identity",
                        icon = Icons.Outlined.Shield,
                        iconTint = Color(0xFF1E7A4A),
                        iconBackground = Color(0xFF1E7A4A).copy(alpha = 0.1f),
                        onClick = { },
                        trailingContent = {
                            Surface(
                                shape = MaterialTheme.shapes.small,
                                color = if (profile.isKycVerified) Color(0xFF1E7A4A).copy(alpha = 0.1f) else Color(0xFFC9952A).copy(alpha = 0.1f)
                            ) {
                                Text(
                                    text = if (profile.isKycVerified) "✓ Verified" else "Pending",
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (profile.isKycVerified) Color(0xFF1E7A4A) else Color(0xFFC9952A)
                                )
                            }
                        }
                    )
                }

                // Security section
                SectionLabel(text = "Security")

                ProfileSectionCard {
                    ProfileToggleRow(
                        title = "Biometric Login",
                        subtitle = "Fingerprint or Face ID",
                        icon = Icons.Outlined.Fingerprint,
                        iconTint = Color(0xFF0B1A3E),
                        iconBackground = Color(0xFF0B1A3E).copy(alpha = 0.07f),
                        checked = profile.isBiometricEnabled,
                        onCheckedChange = { onEvent(ProfileUiEvent.BiometricToggled(it)) }
                    )
                    ProfileSectionDivider()
                    ProfileToggleRow(
                        title = "Payment PIN",
                        subtitle = "Required above $100",
                        icon = Icons.Outlined.Lock,
                        iconTint = Color(0xFFC9952A),
                        iconBackground = Color(0xFFC9952A).copy(alpha = 0.1f),
                        checked = profile.isPaymentPinEnabled,
                        onCheckedChange = { onEvent(ProfileUiEvent.PaymentPinToggled(it)) }
                    )
                    ProfileSectionDivider()
                    ProfileInfoRow(
                        title = "Change Password",
                        subtitle = "Last changed 30 days ago",
                        icon = Icons.Outlined.VpnKey,
                        iconTint = Color(0xFF0E7490),
                        iconBackground = Color(0xFF0E7490).copy(alpha = 0.1f),
                        onClick = { }
                    )
                    ProfileSectionDivider()
                    ProfileInfoRow(
                        title = "Trusted Devices",
                        subtitle = "2 devices connected",
                        icon = Icons.Outlined.PhoneAndroid,
                        iconTint = Color(0xFF0B1A3E),
                        iconBackground = Color(0xFF0B1A3E).copy(alpha = 0.07f),
                        onClick = { }
                    )
                }

                // Preferences section
                SectionLabel(text = "Preferences")

                ProfileSectionCard {
                    ProfileToggleRow(
                        title = "Push Notifications",
                        subtitle = "Payments, messages, alerts",
                        icon = Icons.Outlined.Notifications,
                        iconTint = Color(0xFF0B1A3E),
                        iconBackground = Color(0xFF0B1A3E).copy(alpha = 0.07f),
                        checked = profile.isPushNotificationsEnabled,
                        onCheckedChange = { onEvent(ProfileUiEvent.NotificationsToggled(it)) }
                    )
                    ProfileSectionDivider()
                    ProfileInfoRow(
                        title = "Currency",
                        subtitle = "${profile.currency} — US Dollar",
                        icon = Icons.Outlined.Language,
                        iconTint = Color(0xFFC9952A),
                        iconBackground = Color(0xFFC9952A).copy(alpha = 0.1f),
                        onClick = { }
                    )
                    ProfileSectionDivider()
                    ProfileInfoRow(
                        title = "App Theme",
                        subtitle = "Light mode — Classic",
                        icon = Icons.Outlined.WbSunny,
                        iconTint = Color(0xFF1E7A4A),
                        iconBackground = Color(0xFF1E7A4A).copy(alpha = 0.1f),
                        onClick = { }
                    )
                }

                // Support section
                SectionLabel(text = "Support")

                ProfileSectionCard {
                    ProfileInfoRow(
                        title = "Help & Support",
                        subtitle = "FAQs, live chat, contact us",
                        icon = Icons.Outlined.SupportAgent,
                        iconTint = Color(0xFF0E7490),
                        iconBackground = Color(0xFF0E7490).copy(alpha = 0.1f),
                        onClick = { }
                    )
                    ProfileSectionDivider()
                    ProfileInfoRow(
                        title = "Privacy Policy",
                        subtitle = "Terms & conditions",
                        icon = Icons.Outlined.Description,
                        iconTint = Color(0xFF0B1A3E),
                        iconBackground = Color(0xFF0B1A3E).copy(alpha = 0.07f),
                        onClick = { }
                    )
                }

                // Danger zone
                SectionLabel(text = "Account Actions")

                ProfileDangerZone(
                    onSignOutClicked = { onEvent(ProfileUiEvent.SignOutClicked) },
                    onDeleteAccountClicked = { onEvent(ProfileUiEvent.DeleteAccountClicked) }
                )

                // Version tag
                Text(
                    text = "NovaPay v${profile.appVersion} · Build 2026",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp),
                    fontSize = 11.sp,
                    color = Color(0xFF9AA0B4),
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            }
        }

        // Error snackbar
        uiState.errorMessage?.let { error ->
            Snackbar(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                action = {
                    TextButton(onClick = { onEvent(ProfileUiEvent.ErrorDismissed) }) {
                        Text("Dismiss", color = Color(0xFFC9952A))
                    }
                },
                containerColor = Color(0xFF0B1A3E)
            ) {
                Text(error, color = Color.White)
            }
        }

        // Success snackbar
        uiState.successMessage?.let { message ->
            Snackbar(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                action = {
                    TextButton(onClick = { onEvent(ProfileUiEvent.SuccessDismissed) }) {
                        Text("OK", color = Color(0xFFFDD878))
                    }
                },
                containerColor = Color(0xFF1E7A4A)
            ) {
                Text(message, color = Color.White)
            }
        }
    }

    // Sign out confirmation dialog
    if (uiState.showSignOutDialog) {
        AlertDialog(
            onDismissRequest = { onEvent(ProfileUiEvent.SignOutDismissed) },
            title = { Text("Sign Out", fontWeight = FontWeight.Bold, color = Color(0xFF0B1A3E)) },
            text = { Text("Are you sure you want to sign out? You will need to sign in again.", color = Color(0xFF5A6480)) },
            confirmButton = {
                Button(
                    onClick = { onEvent(ProfileUiEvent.SignOutConfirmed) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0B1A3E))
                ) {
                    Text("Sign Out")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { onEvent(ProfileUiEvent.SignOutDismissed) }) {
                    Text("Cancel", color = Color(0xFF0B1A3E))
                }
            },
            containerColor = Color(0xFFF7F5F0)
        )
    }

    // Delete account confirmation dialog
    if (uiState.showDeleteAccountDialog) {
        AlertDialog(
            onDismissRequest = { onEvent(ProfileUiEvent.DeleteAccountDismissed) },
            title = { Text("Delete Account", fontWeight = FontWeight.Bold, color = Color(0xFFD94040)) },
            text = { Text("This action is permanent and cannot be undone. All your data, transaction history, and wallet balance will be deleted forever.", color = Color(0xFF5A6480)) },
            confirmButton = {
                Button(
                    onClick = { onEvent(ProfileUiEvent.DeleteAccountConfirmed) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD94040))
                ) {
                    Text("Delete Forever")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { onEvent(ProfileUiEvent.DeleteAccountDismissed) }) {
                    Text("Cancel", color = Color(0xFF0B1A3E))
                }
            },
            containerColor = Color(0xFFF7F5F0)
        )
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text.uppercase(),
        modifier = Modifier.padding(start = 22.dp, end = 22.dp, top = 20.dp, bottom = 8.dp),
        fontSize = 10.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF9AA0B4),
        letterSpacing = 1.2.sp
    )
}

@Preview(showBackground = true)
@Composable
fun ProfileContentPreview() {
    val mockProfile = Profile(
        uid = "12345",
        displayName = "Alex Morgan",
        username = "alex.morgan",
        email = "alex@example.com",
        phoneNumber = "+1 234 567 8900",
        bio = "Fintech product developer & designer.",
        avatarUrl = null,
        isKycVerified = true,
        joinedAt = System.currentTimeMillis(),
        wireId = "NP-ALEX99",
        totalSent = 1250.0,
        totalReceived = 3400.0,
        totalTransfers = 42,
        contactCount = 18,
        isBiometricEnabled = true,
        isPaymentPinEnabled = false,
        currency = "USD",
        appVersion = "1.0.0",
        isPushNotificationsEnabled = true
    )
    ProfileContent(
        uiState = ProfileUiState(profile = mockProfile),
        onEvent = {}
    )
}
