package com.example.mylibrary.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mylibrary.data.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RegisterUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirm: String = "",
    val accepted: Boolean = false,
    val showErrors: Boolean = false,
    val registered: Boolean = false
) {
    val nameError: String? get() = if (showErrors && name.isBlank()) "Campo obligatorio" else null
    val emailError: String? get() = if (showErrors && !email.contains("@")) "Correo no válido" else null
    val passwordError: String? get() = if (showErrors && password.length < 8) "Mínimo 8 caracteres" else null
    val confirmError: String? get() =
        if (showErrors && confirm != password) "Las contraseñas no coinciden" else null
    val termsError: Boolean get() = showErrors && !accepted
    val isValid: Boolean get() =
        name.isNotBlank() && email.contains("@") && password.length >= 8 && confirm == password && accepted
}

class RegisterViewModel(private val settingsRepository: SettingsRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun onNameChange(value: String) = _uiState.update { it.copy(name = value) }
    fun onEmailChange(value: String) = _uiState.update { it.copy(email = value) }
    fun onPasswordChange(value: String) = _uiState.update { it.copy(password = value) }
    fun onConfirmChange(value: String) = _uiState.update { it.copy(confirm = value) }
    fun onAcceptedChange(value: Boolean) = _uiState.update { it.copy(accepted = value) }

    fun onRegisterClick() {
        val state = _uiState.value
        if (!state.isValid) {
            _uiState.update { it.copy(showErrors = true) }
            return
        }
        viewModelScope.launch {
            settingsRepository.setUser(state.name.trim(), state.email.trim())
            _uiState.update { it.copy(registered = true) }
        }
    }
}
