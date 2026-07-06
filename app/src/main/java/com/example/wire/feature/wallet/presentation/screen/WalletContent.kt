package com.example.wire.feature.wallet.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wire.core.ui.theme.BrandGold
import com.example.wire.feature.wallet.presentation.components.BalanceCard
import com.example.wire.feature.wallet.presentation.components.SavingsGoalItem
import com.example.wire.feature.wallet.presentation.state.SavingsGoal
import com.example.wire.feature.wallet.presentation.state.WalletUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletContent(
    uiState: WalletUiState,
    onRefresh: () -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background, // Follows WireTheme
        topBar = {
            LargeTopAppBar(
                title = {
                    Text("Wallet",
                        fontWeight = FontWeight.Black,
                        fontSize = 32.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                BalanceCard(
                    amount = uiState.balance?.amount?.toString() ?: "0.00",
                    onSend = {},
                    onReceive = {},
                    onTopUp = {}
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Savings Goals",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    TextButton(onClick = {}) {
                        Text("+ New Goal", color = BrandGold, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Fixed error: items now has access to the component and list
            items(uiState.savingGoals) { goal ->
                SavingsGoalItem(goal)
            }

            item { Spacer(Modifier.height(80.dp)) } // Bottom padding
        }
    }
}
