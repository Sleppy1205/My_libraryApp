package com.example.mylibrary.ui.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylibrary.ui.components.FormField
import com.example.mylibrary.ui.components.PasswordField
import com.example.mylibrary.ui.components.ScreenContainer

/** Único composable que conoce el ViewModel. */
@Composable
fun LoginRoute(
    factory: ViewModelProvider.Factory,
    onLoggedIn: () -> Unit,
    onGoToRegister: () -> Unit
) {
    val viewModel: LoginViewModel = viewModel(factory = factory)
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(state.loggedIn) {
        if (state.loggedIn) onLoggedIn()
    }

    LoginScreen(
        state = state,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onLoginClick = viewModel::onLoginClick,
        onGoToRegister = onGoToRegister
    )
}

/** Composable sin estado propio: solo recibe datos y lambdas. */
@Composable
fun LoginScreen(
    state: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onGoToRegister: () -> Unit
) {
    Scaffold { inner ->
        ScreenContainer(Modifier.padding(inner)) {
            Spacer(Modifier.height(32.dp))
            Icon(
                Icons.Filled.AutoStories, contentDescription = null,
                modifier = Modifier.size(56.dp), tint = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(12.dp))
            Text("Bienvenido de vuelta", style = MaterialTheme.typography.headlineMedium)
            Text(
                "Ingresa a tu biblioteca personal",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(24.dp))
            FormField(
                value = state.email, onValueChange = onEmailChange, label = "Correo electrónico",
                error = state.emailError, keyboardType = KeyboardType.Email
            )
            PasswordField(
                value = state.password, onValueChange = onPasswordChange, label = "Contraseña",
                error = state.passwordError
            )
            Spacer(Modifier.height(16.dp))
            Button(onClick = onLoginClick, modifier = Modifier.fillMaxWidth()) { Text("Ingresar") }
            Spacer(Modifier.height(8.dp))
            Text("¿No tienes cuenta?", style = MaterialTheme.typography.bodyMedium)
            TextButton(onClick = onGoToRegister) { Text("Crear cuenta") }
        }
    }
}
