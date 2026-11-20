package com.example.mylibrary.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mylibrary.data.repository.BookRepository
import com.example.mylibrary.model.Book
import com.example.mylibrary.model.ReadingStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class DetailUiState(
    val book: Book? = null,
    val isLoading: Boolean = true,
    val deleted: Boolean = false
)

class BookDetailViewModel(
    private val bookId: Int,
    private val repository: BookRepository
) : ViewModel() {

    private val deleted = MutableStateFlow(false)

    val uiState: StateFlow<DetailUiState> = combine(
        repository.observeBook(bookId),
        deleted
    ) { book, isDeleted ->
        DetailUiState(book = book, isLoading = false, deleted = isDeleted)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), DetailUiState())

    fun onToggleFavorite() = update { it.copy(favorite = !it.favorite) }

    fun onRatingChange(rating: Int) = update { it.copy(rating = rating) }

    fun onStatusChange(status: ReadingStatus) = update { it.copy(status = status) }

    fun onDelete() {
        viewModelScope.launch {
            repository.deleteBook(bookId)
            deleted.value = true
        }
    }

    private fun update(transform: (Book) -> Book) {
        val current = uiState.value.book ?: return
        viewModelScope.launch { repository.saveBook(transform(current)) }
    }
}
