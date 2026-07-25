package com.example.wire.core.feature.security.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NumericKeypad(
    onNumberClick: (String) -> Unit,
    onDelete: () -> Unit
) {

    val rows = listOf(

        listOf("1","2","3"),
        listOf("4","5","6"),
        listOf("7","8","9"),
        listOf("","0","⌫")
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        rows.forEach { row ->

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                row.forEach { key ->

                    when (key) {

                        "" -> {
                            Spacer(
                                modifier = Modifier.size(72.dp)
                            )
                        }

                        "⌫" -> {

                            FilledTonalButton(
                                onClick = onDelete,
                                modifier = Modifier.size(72.dp)
                            ) {
                                Text("⌫")
                            }
                        }

                        else -> {

                            FilledTonalButton(
                                onClick = {
                                    onNumberClick(key)
                                },
                                modifier = Modifier.size(72.dp)
                            ) {
                                Text(
                                    text = key,
                                    style = MaterialTheme.typography.headlineSmall
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}