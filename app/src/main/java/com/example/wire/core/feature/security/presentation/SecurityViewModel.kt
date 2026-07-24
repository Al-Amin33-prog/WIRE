package com.example.wire.core.feature.security.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wire.core.feature.security.domain.usecase.SecurityUseCases
import com.example.wire.core.feature.security.presentation.effect.SecurityUiEventEffect
import com.example.wire.core.feature.security.presentation.event.SecurityUiEvent
import com.example.wire.core.feature.security.presentation.state.SecurityStep
import com.example.wire.core.feature.security.presentation.state.SecurityUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SecurityViewModel @Inject constructor(
    private val securityUseCases: SecurityUseCases
): ViewModel(){
    private val _uiState = MutableStateFlow(SecurityUiState())
    val uiState = _uiState.asStateFlow()
    private val _events = MutableSharedFlow<SecurityUiEventEffect>()
    val events = _events.asSharedFlow()

    init {
        determineSecurityFlow()
    }

    fun onEvent(event: SecurityUiEvent){
        when(event){
            is SecurityUiEvent.PinChanged -> {
                _uiState.update { it.copy(pin = event.value) }
            }
            is SecurityUiEvent.ConfirmPinChanged -> {
                _uiState.update { it.copy(confirmPin = event.value, pinError = null) }
            }
            is SecurityUiEvent.CreatePinClicked -> {
                val state = _uiState.value
                if (state.step == SecurityStep.SetPin) {
                    _uiState.update {
                        it.copy(
                            step = SecurityStep.ConfirmPin,
                            confirmPin = "",
                            pinError = null
                        )
                    }
                } else if (state.step == SecurityStep.ConfirmPin) {
                    if (state.pin == state.confirmPin) {
                        createPin()
                    } else {
                        _uiState.update {
                            it.copy(
                                confirmPin = "",
                                pinError = "PINs do not match"
                            )
                        }
                    }
                }
            }
            is SecurityUiEvent.EnableBiometricClicked -> {
                _uiState.update { it.copy(biometricLoading = true) }
                viewModelScope.launch {
                    _events.emit(SecurityUiEventEffect.DismissBiometricSheet)
                    delay(350)
                    _events.emit(SecurityUiEventEffect.LaunchedBiometricPrompt)
                }
            }
            is SecurityUiEvent.BiometricAuthenticationSucceeded -> {
                enableBiometric()
            }
            is SecurityUiEvent.BiometricFailed -> {
                _uiState.update { 
                    it.copy(
                        biometricLoading = false,
                        errorMessage = if (event.message == "CANCELLED") null else event.message
                    ) 
                }
                if (event.message != "CANCELLED") {
                    skipBiometric() // Fallback to skip if it fails hard
                }
            }
            is SecurityUiEvent.DismissEnrollment -> {
              skipBiometric()
            }
            else -> Unit
        }
    }

    private fun createPin() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            securityUseCases.createPin(_uiState.value.pin)
            
            // After PIN, check if we should enroll biometrics
            val settings = securityUseCases.getSecuritySettings()
            if ( !settings.isBiometricEnabled) {
                _uiState.update { 
                    it.copy(
                        hasPin = true,
                        step = SecurityStep.EnrollBiometric,
                        isLoading = false
                    )
                }
            } else {
                skipBiometric()
            }
        }
    }

    private fun enableBiometric() {
        viewModelScope.launch {
            securityUseCases.enableBiometric()
            _uiState.update { it.copy(biometricLoading = false) }
            delay(250)
            _events.emit(SecurityUiEventEffect.NavigateToMainShell)
        }
    }

    private fun skipBiometric() {
        viewModelScope.launch {
            _events.emit(SecurityUiEventEffect.NavigateToMainShell)
        }
    }

    private fun determineSecurityFlow() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val settings = securityUseCases.getSecuritySettings()
            
            if (!settings.hasPin) {
                _uiState.update {
                    it.copy(
                        hasPin = false,
                        step = SecurityStep.SetPin,
                        isLoading = false
                    )
                }
            } else {
                // User already has PIN, navigate immediately
                _events.emit(SecurityUiEventEffect.NavigateToMainShell)
            }
        }
    }
}
