package com.example.wire.feature.contacts.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.wire.R
import com.example.wire.feature.contacts.components.ContactItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactSelectionContent(
    uiState: ContactUiState,
    onBackClick: () -> Unit,
    onContactSelected: (String) -> Unit,
    onGrantPermissionClick: () -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background, // Cream Background
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.select_contact), fontWeight = FontWeight.Black) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->
        Column(
            modifier =
                Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .systemBarsPadding()
        ) {
            if (uiState.isPermissionDenied) {
                PermissionDeniedContent(onGrantClick = onGrantPermissionClick)
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(uiState.contacts) { contact ->
                        ContactItem(
                            contact = contact,
                            onClick = { onContactSelected(contact.id) }
                        )
                    }
                }
            }
        }
    }
}