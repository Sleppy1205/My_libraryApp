package com.example.mylibrary.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylibrary.model.Genres
import com.example.mylibrary.model.ReadingStatus
import com.example.mylibrary.ui.components.AppTopBar
import com.example.mylibrary.ui.components.DropdownField
import com.example.mylibrary.ui.components.FormField
import com.example.mylibrary.ui.components.ScreenContainer

/** Sirve para agregar un libro nuevo y para editar uno existente. */
@Composable
fun BookFormRoute(
    factory: ViewModelProvider.Factory,
    onBack: () -> Unit
) {
    val viewModel: BookFormViewModel = viewModel(factory = factory)
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(state.saved) {
        if (state.saved) onBack()
    }

    BookFormScreen(
        state = state,
        onTitleChange = viewModel::onTitleChange,
        onAuthorChange = viewModel::onAuthorChange,
        onGenreChange = viewModel::onGenreChange,
        onPagesChange = viewModel::onPagesChange,
        onStatusChange = viewModel::onStatusChange,
        onSynopsisChange = viewModel::onSynopsisChange,
        onSaveClick = viewModel::onSaveClick,
        onBack = onBack
    )
}

@Composable
fun BookFormScreen(
    state: BookFormUiState,
    onTitleChange: (String) -> Unit,
    onAuthorChange: (String) -> Unit,
    onGenreChange: (String) -> Unit,
    onPagesChange: (String) -> Unit,
    onStatusChange: (ReadingStatus) -> Unit,
    onSynopsisChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            AppTopBar(
                title = if (state.isEditing) "Editar libro" else "Agregar libro",
                onBack = onBack
            )
        }
    ) { inner ->
        ScreenContainer(Modifier.padding(inner)) {
            FormField(state.title, onTitleChange, "Título", error = state.titleError)
            FormField(state.author, onAuthorChange, "Autor", error = state.authorError)
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                DropdownField(
                    label = "Género", value = state.genre, options = Genres,
                    onSelect = onGenreChange, modifier = Modifier.weight(1f)
                )
                FormField(
                    state.pages, onPagesChange, "N.º páginas",
                    modifier = Modifier.weight(1f),
                    error = state.pagesError,
                    keyboardType = KeyboardType.Number
                )
            }
            DropdownField(
                label = "Estado de lectura",
                value = state.status.label,
                options = ReadingStatus.values().map { it.label },
                onSelect = { sel -> onStatusChange(ReadingStatus.values().first { it.label == sel }) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            FormField(
                state.synopsis, onSynopsisChange, "Sinopsis (opcional)",
                singleLine = false, minLines = 3
            )
            Spacer(Modifier.height(16.dp))
            Button(onClick = onSaveClick, modifier = Modifier.fillMaxWidth()) { Text("Guardar libro") }
        }
    }
}
