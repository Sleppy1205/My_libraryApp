package com.example.mylibrary.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mylibrary.data.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val showErrors: Boolean = false,
    val loggedIn: Boolean = false
) {
    val emailError: String? get() = if (showErrors && email.isBlank()) "Campo obligatorio" else null
    val passwordError: String? get() = if (showErrors && password.isBlank()) "Campo obligatorio" else null
}

class LoginViewModel(private val settingsRepository: SettingsRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEmailChange(value: String) = _uiState.update { it.copy(email = value) }
    fun onPasswordChange(value: String) = _uiState.update { it.copy(password = value) }

    fun onLoginClick() {
        val state = _uiState.value
        if (state.email.isBlank() || state.password.isBlank()) {
            _uiState.update { it.copy(showErrors = true) }
            return
        }
        viewModelScope.launch {
            val email = state.email.trim()
            // Todavía no hay cuentas guardadas: el nombre se toma de lo que va antes de la @.
            if (settingsRepository.settings.value.userEmail != email) {
                val name = email.substringBefore("@").replaceFirstChar { it.uppercase() }
                settingsRepository.setUser(name, email)
            }
            _uiState.update { it.copy(loggedIn = true) }
        }
    }
}
