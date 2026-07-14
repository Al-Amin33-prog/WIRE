package com.example.wire.feature.auth.presentation



import androidx.lifecycle.ViewModel
import com.example.wire.feature.auth.presentation.authgate.AuthGateUiState
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class AuthGateViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<AuthGateUiState>(
            AuthGateUiState.Loading
        )

    val uiState: StateFlow<AuthGateUiState> = _uiState

    init {

        if (firebaseAuth.currentUser != null) {

            _uiState.value =
                AuthGateUiState.Authenticated

        } else {

            _uiState.value =
                AuthGateUiState.Unauthenticated
        }
    }
}