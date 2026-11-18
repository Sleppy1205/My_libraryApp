package com.example.mylibrary.data.repository

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.example.mylibrary.data.local.AutorEntity
import com.example.mylibrary.data.local.BookDao
import com.example.mylibrary.data.local.GeneroEntity
import com.example.mylibrary.data.local.LibroConDetalle
import com.example.mylibrary.data.local.LibroEntity
import com.example.mylibrary.data.local.SeedData
import com.example.mylibrary.model.Book
import com.example.mylibrary.model.ReadingStatus
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

/**
 * Implementación con Room. Room es la fuente única de verdad (SSOT):
 * toda lectura y escritura pasa por aquí y se delega a Dispatchers.IO.
 */
class RoomBookRepository(
    private val dao: BookDao,
    private val io: CoroutineDispatcher = Dispatchers.IO
) : BookRepository {

    override fun observeBooks(): Flow<List<Book>> =
        dao.observeAll()
            .map { rows -> rows.map { it.toBook() } }
            .flowOn(io)

    override fun observeBook(id: Int): Flow<Book?> =
        dao.observeById(id)
            .map { row -> row?.toBook() }
            .flowOn(io)

    override suspend fun saveBook(book: Book) {
        withContext(io) {
            val idAutor = ensureAutor(book.author.trim())
            val idGenero = ensureGenero(book.genre)
            val previous = if (book.id != 0) dao.findLibro(book.id) else null
            val entity = LibroEntity(
                idLibro = book.id,
                titulo = book.title,
                idAutor = idAutor,
                idGenero = idGenero,
                numeroPaginas = book.pages,
                estado = book.status.name,
                calificacion = book.rating,
                favorito = book.favorite,
                sinopsis = book.synopsis,
                colorPortada = book.coverColor.toArgb(),
                fechaRegistro = previous?.fechaRegistro ?: System.currentTimeMillis()
            )
            if (book.id == 0) dao.insertLibro(entity) else dao.updateLibro(entity)
        }
    }

    override suspend fun deleteBook(id: Int) {
        withContext(io) { dao.deleteLibro(id) }
    }

    /** Inserta los libros de ejemplo (solo se llama la primera vez). */
    suspend fun seedSampleBooks() {
        SeedData.books.forEach { saveBook(it) }
    }

    private suspend fun ensureAutor(nombre: String): Int {
        dao.insertAutor(AutorEntity(nombre = nombre))
        return dao.findAutorId(nombre) ?: error("No se pudo guardar el autor")
    }

    private suspend fun ensureGenero(nombre: String): Int {
        dao.insertGenero(GeneroEntity(nombre = nombre))
        return dao.findGeneroId(nombre) ?: error("No se pudo guardar el género")
    }
}

private fun LibroConDetalle.toBook() = Book(
    id = libro.idLibro,
    title = libro.titulo,
    author = autorNombre,
    genre = generoNombre,
    pages = libro.numeroPaginas,
    status = ReadingStatus.valueOf(libro.estado),
    rating = libro.calificacion,
    favorite = libro.favorito,
    synopsis = libro.sinopsis,
    coverColor = Color(libro.colorPortada)
)
