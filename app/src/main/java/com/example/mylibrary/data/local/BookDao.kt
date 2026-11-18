package com.example.mylibrary.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

private const val SELECT_DETALLE =
    "SELECT l.*, a.nombre AS autorNombre, g.nombre AS generoNombre " +
        "FROM libro l " +
        "INNER JOIN autor a ON l.idAutor = a.idAutor " +
        "INNER JOIN genero g ON l.idGenero = g.idGenero"

@Dao
interface BookDao {

    @Query(SELECT_DETALLE + " ORDER BY l.idLibro DESC")
    fun observeAll(): Flow<List<LibroConDetalle>>

    @Query(SELECT_DETALLE + " WHERE l.idLibro = :id")
    fun observeById(id: Int): Flow<LibroConDetalle?>

    @Query("SELECT * FROM libro WHERE idLibro = :id")
    suspend fun findLibro(id: Int): LibroEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAutor(autor: AutorEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertGenero(genero: GeneroEntity): Long

    @Query("SELECT idAutor FROM autor WHERE nombre = :nombre")
    suspend fun findAutorId(nombre: String): Int?

    @Query("SELECT idGenero FROM genero WHERE nombre = :nombre")
    suspend fun findGeneroId(nombre: String): Int?

    @Insert
    suspend fun insertLibro(libro: LibroEntity): Long

    @Update
    suspend fun updateLibro(libro: LibroEntity)

    @Query("DELETE FROM libro WHERE idLibro = :id")
    suspend fun deleteLibro(id: Int)
}
