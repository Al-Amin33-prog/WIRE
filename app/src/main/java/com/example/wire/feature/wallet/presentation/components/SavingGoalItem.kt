package com.example.wire.feature.wallet.presentation.components



import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wire.core.ui.theme.BrandGold
import com.example.wire.feature.wallet.presentation.state.SavingsGoal

@Composable
fun SavingsGoalItem(goal: SavingsGoal) {
    val progress = (goal.savedAmount / goal.targetAmount).toFloat()

    // We use MaterialTheme.colorScheme.surface so it turns dark in Dark Mode
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 0.5.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon Circle
            Surface(
                modifier = Modifier.size(48.dp),
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.background
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(goal.iconEmoji, fontSize = 24.sp)
                }
            }

            Spacer(Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(goal.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text("${(progress * 100).toInt()}%", fontWeight = FontWeight.Bold)
                }

                Spacer(Modifier.height(8.dp))

                // Progress Bar
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth().height(6.dp),
                    color = BrandGold,
                    trackColor = Color.Gray.copy(alpha = 0.2f),
                    strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
                )

                Spacer(Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("$${goal.savedAmount.toInt()} saved", fontSize = 12.sp, color = Color.Gray)
                    Text("$${goal.targetAmount.toInt()} goal", fontSize = 12.sp, color = Color.Gray)
                }
            }
        }
    }
}
