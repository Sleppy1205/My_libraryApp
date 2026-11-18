package com.example.mylibrary.data.local

import com.example.mylibrary.model.Book
import com.example.mylibrary.model.CoverColors
import com.example.mylibrary.model.ReadingStatus

/** Libros de ejemplo que se insertan en la base la primera vez que se abre la app. */
object SeedData {
    val books: List<Book> = listOf(
        Book(0, "Cien años de soledad", "Gabriel García Márquez", "Novela", 471,
            ReadingStatus.LEYENDO, 4, true,
            "La historia de la familia Buendía a lo largo de siete generaciones en Macondo.", CoverColors[0]),
        Book(0, "El Principito", "Antoine de Saint-Exupéry", "Novela", 96,
            ReadingStatus.TERMINADO, 5, true,
            "Un piloto varado en el desierto conoce a un pequeño príncipe de otro planeta.", CoverColors[1]),
        Book(0, "1984", "George Orwell", "Ciencia ficción", 328,
            ReadingStatus.PENDIENTE, 0, false,
            "Una sociedad vigilada por el Gran Hermano donde pensar distinto es un delito.", CoverColors[2]),
        Book(0, "Rayuela", "Julio Cortázar", "Novela", 600,
            ReadingStatus.PENDIENTE, 0, false,
            "Una novela que se puede leer en más de un orden, entre París y Buenos Aires.", CoverColors[3]),
        Book(0, "La Odisea", "Homero", "Poesía", 416,
            ReadingStatus.LEYENDO, 0, false,
            "El largo viaje de Ulises de regreso a Ítaca tras la guerra de Troya.", CoverColors[4]),
        Book(0, "Fahrenheit 451", "Ray Bradbury", "Ciencia ficción", 249,
            ReadingStatus.TERMINADO, 4, false,
            "En un futuro donde los libros están prohibidos, un bombero empieza a dudar.", CoverColors[5])
    )
}
