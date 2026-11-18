package com.example.mylibrary.data.repository

import com.example.mylibrary.model.Book
import kotlinx.coroutines.flow.Flow

/** Contrato de la capa de datos. Los ViewModels solo conocen esta interfaz. */
interface BookRepository {
    fun observeBooks(): Flow<List<Book>>
    fun observeBook(id: Int): Flow<Book?>

    /** Si book.id == 0 crea un libro nuevo; si no, actualiza el existente. */
    suspend fun saveBook(book: Book)

    suspend fun deleteBook(id: Int)
}
