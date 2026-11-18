package com.example.mylibrary.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylibrary.ui.components.AppTopBar
import com.example.mylibrary.ui.components.FormField
import com.example.mylibrary.ui.components.PasswordField
import com.example.mylibrary.ui.components.ScreenContainer

@Composable
fun RegisterRoute(
    factory: ViewModelProvider.Factory,
    onBack: () -> Unit,
    onRegistered: () -> Unit
) {
    val viewModel: RegisterViewModel = viewModel(factory = factory)
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(state.registered) {
        if (state.registered) onRegistered()
    }

    RegisterScreen(
        state = state,
        onNameChange = viewModel::onNameChange,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onConfirmChange = viewModel::onConfirmChange,
        onAcceptedChange = viewModel::onAcceptedChange,
        onRegisterClick = viewModel::onRegisterClick,
        onBack = onBack
    )
}

@Composable
fun RegisterScreen(
    state: RegisterUiState,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmChange: (String) -> Unit,
    onAcceptedChange: (Boolean) -> Unit,
    onRegisterClick: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(topBar = { AppTopBar(title = "", onBack = onBack) }) { inner ->
        ScreenContainer(Modifier.padding(inner)) {
            Text("Crea tu cuenta", style = MaterialTheme.typography.headlineMedium)
            Text(
                "Empieza a organizar tu biblioteca",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(24.dp))
            FormField(state.name, onNameChange, "Nombre", error = state.nameError)
            FormField(
                state.email, onEmailChange, "Correo electrónico",
                error = state.emailError, keyboardType = KeyboardType.Email
            )
            PasswordField(state.password, onPasswordChange, "Contraseña", error = state.passwordError)
            PasswordField(state.confirm, onConfirmChange, "Confirmar contraseña", error = state.confirmError)
            Row(
                modifier = Modifier.fillMaxWidth().clickable { onAcceptedChange(!state.accepted) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(checked = state.accepted, onCheckedChange = onAcceptedChange)
                Text(
                    "Acepto los términos y condiciones",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (state.termsError) MaterialTheme.colorScheme.error
                    else MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(Modifier.height(16.dp))
            Button(onClick = onRegisterClick, modifier = Modifier.fillMaxWidth()) { Text("Crear cuenta") }
            Spacer(Modifier.height(8.dp))
            Text("¿Ya tienes una cuenta?", style = MaterialTheme.typography.bodyMedium)
            TextButton(onClick = onBack) { Text("Iniciar sesión") }
        }
    }
}
