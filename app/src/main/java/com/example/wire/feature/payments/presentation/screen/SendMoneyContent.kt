package com.example.wire.feature.payments.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AlternateEmail // FIXED: Missing Import
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.wire.feature.payments.presentation.component.*
import com.example.wire.feature.payments.presentation.event.PaymentUiEvent
import com.example.wire.feature.payments.presentation.state.PaymentMode
import com.example.wire.feature.payments.presentation.state.PaymentTab
import com.example.wire.feature.payments.presentation.state.PaymentUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SendMoneyContent(
    uiState: PaymentUiState,
    onEvent: (PaymentUiEvent) -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = if (uiState.paymentMode == PaymentMode.SEND) "Send Money" else "Request Money",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        bottomBar = {
            if (uiState.selectedTab == PaymentTab.KEYPAD) {
                Button(
                    // SYMPHONY: We trigger ConfirmClicked which now handles the Security Gate in ViewModel
                    onClick = { onEvent(PaymentUiEvent.ConfirmClicked) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        // THEME CHECK: Use primary for Send, Secondary/Accent for Request
                        containerColor = if (uiState.paymentMode == PaymentMode.SEND)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.secondary
                    )
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White)
                    } else {
                        Text(
                            text = if (uiState.paymentMode == PaymentMode.SEND) "Continue" else "Request Funds",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .systemBarsPadding()
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PaymentTabSwitcher(
                selectedTab = uiState.selectedTab,
                onTabSelected = { onEvent(PaymentUiEvent.TabChanged(it)) }
            )

            Spacer(Modifier.height(24.dp))

            if (uiState.selectedTab == PaymentTab.KEYPAD) {
                // SECTION: Recipient Selection
                if (uiState.recipientId.isEmpty()) {
                    // THE PASTE FIELD: Shows if no one is selected from a chat
                    OutlinedTextField(
                        value = uiState.manualHandleEntry,
                        onValueChange = { onEvent(PaymentUiEvent.ManualHandleChanged(it)) },
                        label = { Text("Enter Handle (e.g. shaba.wire)") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        shape = RoundedCornerShape(16.dp),
                        leadingIcon = { Icon(Icons.Default.AlternateEmail, null) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f)
                        )
                    )
                } else {
                    RecipientCard(
                        name = uiState.recipientName,
                        username = uiState.recipientName.lowercase().replace(" ", ""),
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                }

                Spacer(Modifier.height(32.dp))

                AmountDisplay(
                    amount = uiState.amount,
                    availableBalance = "4,280.50"
                )

                Spacer(Modifier.weight(1f)) // Push keypad to bottom

                NumPad(
                    onNumberClick = { onEvent(PaymentUiEvent.NumberClicked(it)) },
                    onBackspace = { onEvent(PaymentUiEvent.BackspaceClicked) }
                )
            } else {
                // SECTION: QR View
                QrCodeView(
                    userHandle = uiState.currentUserHandle,
                    onShareClick = { /* Implement System Share */ }
                )
            }
        }

        // DESIGN LOGIC: The Confirm Sheet is an overlay, not a separate screen.
        if (uiState.showConfirmSheet) {
            ConfirmBottomSheet(
                uiState = uiState,
                onConfirm = { onEvent(PaymentUiEvent.ConfirmClicked) },
                onDismiss = { onEvent(PaymentUiEvent.DismissSheet) }
            )
        }
    }
}