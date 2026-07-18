package com.example.wire.core.feature.security.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wire.core.feature.security.domain.usecase.SecurityUseCases
import com.example.wire.core.feature.security.presentation.event.SecurityUiEvent
import com.example.wire.core.feature.security.presentation.state.SecurityStep
import com.example.wire.core.feature.security.presentation.state.SecurityUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
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

    init {
     determineSecurityFlow()
    }

    fun onEvent(event: SecurityUiEvent){
        when(event){
            is SecurityUiEvent.PinChanged -> {
                _uiState.update {
                    it.copy(pin = event.value)
                }
            }
            is SecurityUiEvent.CreatePinClicked -> {
                createPin()
            }
            is SecurityUiEvent.DisableBiometricClicked -> {
                viewModelScope.launch {
                    securityUseCases.disableBiometric()

                }
            }
            is SecurityUiEvent.EnableBiometricClicked -> {
                _uiState.update {
                    it.copy(
                        step = SecurityStep.RequestBiometricAuthentication
                    )
                }
            }
            is SecurityUiEvent.BiometricFailed -> {
                _uiState.update {
                    it.copy(
                       errorMessage = event.message,
                        step = SecurityStep.EnrollBiometric
                    )
                }

            }
            is SecurityUiEvent.DismissEnrollment -> {
              skipBiometric()
            }
            is SecurityUiEvent.BiometricAuthenticationSucceeded -> {
                enableBiometric()
            }
        }
    }
    private fun enableBiometric(){
        viewModelScope.launch {
            securityUseCases.enableBiometric()
            _uiState.update {
                it.copy(
                    biometricEnabled = true,
                    step = SecurityStep.Completed
                )
            }
        }
    }
    private fun skipBiometric(){
        _uiState.update {
            it.copy(
               step = SecurityStep.Completed
            )
        }
    }
    private fun determineSecurityFlow(){
        viewModelScope.launch {
            val settings = securityUseCases.getSecuritySettings()
            when{
                !settings.hasPin->{
                    _uiState.update {
                        it.copy(
                          hasPin = false,
                            biometricEnabled = settings.isBiometricEnabled,
                            step = SecurityStep.SetPin,
                            isLoading = false
                        )
                    }
                }else->{
                    _uiState.update {
                        it.copy(

                            biometricEnabled = settings.isBiometricEnabled,
                            step = SecurityStep.Completed,
                            isLoading = false
                        )
                    }
                }
            }

        }
    }


    private fun createPin(){
        viewModelScope.launch {
            securityUseCases.createPin(
                _uiState.value.pin
            )
            _uiState.update {
                it.copy(
                    hasPin = true,
                   step = SecurityStep.EnrollBiometric
                )
            }
        }
    }
}