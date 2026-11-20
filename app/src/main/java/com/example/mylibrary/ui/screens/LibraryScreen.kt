package com.example.mylibrary.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylibrary.model.Book
import com.example.mylibrary.ui.components.AppTopBar
import com.example.mylibrary.ui.components.BookCover
import com.example.mylibrary.ui.theme.Cream
import com.example.mylibrary.ui.theme.Terracotta

@Composable
fun LibraryRoute(
    factory: ViewModelProvider.Factory,
    onBookClick: (Int) -> Unit,
    onAddBook: () -> Unit,
    onOpenSettings: () -> Unit
) {
    val viewModel: LibraryViewModel = viewModel(factory = factory)
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val query by viewModel.query.collectAsStateWithLifecycle()

    LibraryScreen(
        state = state,
        query = query,
        onQueryChange = viewModel::onQueryChange,
        onFilterChange = viewModel::onFilterChange,
        onBookClick = onBookClick,
        onAddBook = onAddBook,
        onOpenSettings = onOpenSettings
    )
}

@Composable
fun LibraryScreen(
    state: LibraryUiState,
    query: String,
    onQueryChange: (String) -> Unit,
    onFilterChange: (LibraryFilter) -> Unit,
    onBookClick: (Int) -> Unit,
    onAddBook: () -> Unit,
    onOpenSettings: () -> Unit
) {
    Scaffold(
        topBar = {
            AppTopBar(
                title = "Mi biblioteca",
                actions = {
                    IconButton(onClick = onOpenSettings) {
                        Icon(Icons.Filled.Settings, contentDescription = "Configuración")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddBook) {
                Icon(Icons.Filled.Add, contentDescription = "Agregar libro")
            }
        }
    ) { inner ->
        Column(
            Modifier
                .padding(inner)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                "${state.total} libros · ${state.reading} en lectura",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Buscar por título o autor") },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                singleLine = true,
                shape = MaterialTheme.shapes.extraLarge
            )
            Spacer(Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(LibraryFilter.values().toList()) { f ->
                    FilterChip(
                        selected = state.filter == f,
                        onClick = { onFilterChange(f) },
                        label = { Text(f.label) }
                    )
                }
            }
            when {
                state.isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }

                state.books.isEmpty() -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        "No hay libros para mostrar",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                else -> LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 120.dp),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(top = 12.dp, bottom = 88.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(state.books, key = { it.id }) { book ->
                        BookGridItem(book = book, onClick = { onBookClick(book.id) })
                    }
                }
            }
        }
    }
}

@Composable
private fun BookGridItem(book: Book, onClick: () -> Unit) {
    Column(
        Modifier
            .clip(MaterialTheme.shapes.medium)
            .clickable(onClick = onClick)
    ) {
        Box {
            BookCover(book, Modifier.fillMaxWidth())
            if (book.favorite) {
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(6.dp),
                    shape = CircleShape,
                    color = Cream.copy(alpha = 0.92f)
                ) {
                    Icon(
                        Icons.Filled.Favorite, contentDescription = "Favorito",
                        modifier = Modifier.padding(4.dp).size(16.dp), tint = Terracotta
                    )
                }
            }
        }
        Spacer(Modifier.height(6.dp))
        Text(
            book.title, style = MaterialTheme.typography.labelLarge,
            maxLines = 1, overflow = TextOverflow.Ellipsis
        )
        Text(
            book.status.label, style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary
        )
    }
}
