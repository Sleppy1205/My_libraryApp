package com.example.mylibrary.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mylibrary.data.repository.BookRepository
import com.example.mylibrary.model.Book
import com.example.mylibrary.model.CoverColors
import com.example.mylibrary.model.Genres
import com.example.mylibrary.model.ReadingStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** Identificador que significa "libro nuevo". */
const val NEW_BOOK_ID = -1

data class BookFormUiState(
    val isEditing: Boolean = false,
    val title: String = "",
    val author: String = "",
    val genre: String = Genres.first(),
    val pages: String = "",
    val status: ReadingStatus = ReadingStatus.PENDIENTE,
    val synopsis: String = "",
    val showErrors: Boolean = false,
    val saved: Boolean = false
) {
    private val pagesValid: Boolean get() = (pages.toIntOrNull() ?: 0) > 0

    val titleError: String? get() = if (showErrors && title.isBlank()) "Campo obligatorio" else null
    val authorError: String? get() = if (showErrors && author.isBlank()) "Campo obligatorio" else null
    val pagesError: String? get() = if (showErrors && !pagesValid) "Número no válido" else null
    val isValid: Boolean get() = title.isNotBlank() && author.isNotBlank() && pagesValid
}

class BookFormViewModel(
    private val bookId: Int,
    private val repository: BookRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BookFormUiState(isEditing = bookId != NEW_BOOK_ID))
    val uiState: StateFlow<BookFormUiState> = _uiState.asStateFlow()

    private var existing: Book? = null

    init {
        if (bookId != NEW_BOOK_ID) {
            viewModelScope.launch {
                repository.observeBook(bookId).first()?.let { book ->
                    existing = book
                    _uiState.update {
                        it.copy(
                            title = book.title,
                            author = book.author,
                            genre = book.genre,
                            pages = book.pages.toString(),
                            status = book.status,
                            synopsis = book.synopsis
                        )
                    }
                }
            }
        }
    }

    fun onTitleChange(value: String) = _uiState.update { it.copy(title = value) }
    fun onAuthorChange(value: String) = _uiState.update { it.copy(author = value) }
    fun onGenreChange(value: String) = _uiState.update { it.copy(genre = value) }
    fun onPagesChange(value: String) = _uiState.update { it.copy(pages = value.filter(Char::isDigit)) }
    fun onStatusChange(value: ReadingStatus) = _uiState.update { it.copy(status = value) }
    fun onSynopsisChange(value: String) = _uiState.update { it.copy(synopsis = value) }

    fun onSaveClick() {
        val state = _uiState.value
        if (!state.isValid) {
            _uiState.update { it.copy(showErrors = true) }
            return
        }
        viewModelScope.launch {
            val base = existing
            repository.saveBook(
                Book(
                    id = base?.id ?: 0,
                    title = state.title.trim(),
                    author = state.author.trim(),
                    genre = state.genre,
                    pages = state.pages.toInt(),
                    status = state.status,
                    rating = base?.rating ?: 0,
                    favorite = base?.favorite ?: false,
                    synopsis = state.synopsis.trim(),
                    coverColor = base?.coverColor ?: CoverColors.random()
                )
            )
            _uiState.update { it.copy(saved = true) }
        }
    }
}
