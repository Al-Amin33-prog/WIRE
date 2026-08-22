package com.example.wire.feature.contacts.presentation

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.wire.core.ui.theme.WireTheme
import com.example.wire.feature.contacts.domain.model.ContactUser

@Composable
fun ContactSelectionScreen(
    onContactSelected: (String) -> Unit,
    onBackClick: () -> Unit,
    viewModel: ContactViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Permission Logic (Stateful Side Effect)
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        viewModel.syncContacts(hasPermission = isGranted)
    }
    WireTheme {
        ContactSelectionContent(
            uiState = uiState,
            onBackClick = onBackClick,
            onContactSelected = onContactSelected,
            onGrantPermissionClick = {
                permissionLauncher.launch(Manifest.permission.READ_CONTACTS)
            }
        )
    }


}



@Preview(showBackground = true)
@Composable
fun ContactSelectionPreview() {
    WireTheme(darkTheme = false) { // NovaPay Light Mode
        ContactSelectionContent(
            uiState = ContactUiState(
                contacts = listOf(
                    ContactUser("1", "Sarah K.", 0xFF2ECC71.toInt()),
                    ContactUser("2", "Marcus T.", 0xFF3498DB.toInt())
                )
            ),
            onBackClick = {},
            onContactSelected = {},
            onGrantPermissionClick = {}
        )
    }
}