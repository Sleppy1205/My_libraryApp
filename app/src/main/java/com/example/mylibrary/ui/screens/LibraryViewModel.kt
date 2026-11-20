package com.example.mylibrary.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mylibrary.data.repository.BookRepository
import com.example.mylibrary.data.repository.SettingsRepository
import com.example.mylibrary.model.Book
import com.example.mylibrary.model.ReadingStatus
import com.example.mylibrary.model.SortOrder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

enum class LibraryFilter(val label: String) {
    TODOS("Todos"),
    LEYENDO("Leyendo"),
    PENDIENTE("Pendiente"),
    TERMINADO("Terminado"),
    FAVORITOS("Favoritos")
}

data class LibraryUiState(
    val books: List<Book> = emptyList(),
    val total: Int = 0,
    val reading: Int = 0,
    val filter: LibraryFilter = LibraryFilter.TODOS,
    val isLoading: Boolean = true
)

class LibraryViewModel(
    bookRepository: BookRepository,
    settingsRepository: SettingsRepository
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val selectedFilter = MutableStateFlow(LibraryFilter.TODOS)

    val uiState: StateFlow<LibraryUiState> = combine(
        bookRepository.observeBooks(),
        _query,
        selectedFilter,
        settingsRepository.settings.map { it.sortOrder }.distinctUntilChanged()
    ) { books, q, filter, order ->
        val visible = books
            .filter { book ->
                val matchesQuery = q.isBlank() ||
                    book.title.contains(q, ignoreCase = true) ||
                    book.author.contains(q, ignoreCase = true)
                val matchesFilter = when (filter) {
                    LibraryFilter.TODOS -> true
                    LibraryFilter.LEYENDO -> book.status == ReadingStatus.LEYENDO
                    LibraryFilter.PENDIENTE -> book.status == ReadingStatus.PENDIENTE
                    LibraryFilter.TERMINADO -> book.status == ReadingStatus.TERMINADO
                    LibraryFilter.FAVORITOS -> book.favorite
                }
                matchesQuery && matchesFilter
            }
            .let { list ->
                when (order) {
                    SortOrder.TITULO -> list.sortedBy { it.title.lowercase() }
                    SortOrder.AUTOR -> list.sortedBy { it.author.lowercase() }
                    SortOrder.RECIENTES -> list.sortedByDescending { it.id }
                }
            }
        LibraryUiState(
            books = visible,
            total = books.size,
            reading = books.count { it.status == ReadingStatus.LEYENDO },
            filter = filter,
            isLoading = false
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), LibraryUiState())

    fun onQueryChange(value: String) {
        _query.value = value
    }

    fun onFilterChange(filter: LibraryFilter) {
        selectedFilter.value = filter
    }
}
