package com.example.mylibrary.data.local

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/** Tabla maestra: géneros. */
@Entity(tableName = "genero", indices = [Index(value = ["nombre"], unique = true)])
data class GeneroEntity(
    @PrimaryKey(autoGenerate = true) val idGenero: Int = 0,
    val nombre: String,
    val descripcion: String = ""
)

/** Tabla maestra: autores. */
@Entity(tableName = "autor", indices = [Index(value = ["nombre"], unique = true)])
data class AutorEntity(
    @PrimaryKey(autoGenerate = true) val idAutor: Int = 0,
    val nombre: String
)

/** Tabla transaccional: libros. */
@Entity(
    tableName = "libro",
    foreignKeys = [
        ForeignKey(
            entity = AutorEntity::class,
            parentColumns = ["idAutor"],
            childColumns = ["idAutor"],
            onDelete = ForeignKey.RESTRICT
        ),
        ForeignKey(
            entity = GeneroEntity::class,
            parentColumns = ["idGenero"],
            childColumns = ["idGenero"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [Index("idAutor"), Index("idGenero")]
)
data class LibroEntity(
    @PrimaryKey(autoGenerate = true) val idLibro: Int = 0,
    val titulo: String,
    val idAutor: Int,
    val idGenero: Int,
    val numeroPaginas: Int,
    val estado: String,
    val calificacion: Int,
    val favorito: Boolean,
    val sinopsis: String,
    val colorPortada: Int,
    val fechaRegistro: Long
)

/** Resultado del JOIN libro + autor + género. */
data class LibroConDetalle(
    @Embedded val libro: LibroEntity,
    val autorNombre: String,
    val generoNombre: String
)
