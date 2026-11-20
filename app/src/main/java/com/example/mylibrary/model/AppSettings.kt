package com.example.mylibrary.model

/** Preferencias de la app y datos de la sesión actual. */
data class AppSettings(
    val darkTheme: Boolean = false,
    val sortOrder: SortOrder = SortOrder.RECIENTES,
    val language: String = "Español",
    val userName: String = "",
    val userEmail: String = ""
)
