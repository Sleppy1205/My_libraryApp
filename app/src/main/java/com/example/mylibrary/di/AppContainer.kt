package com.example.mylibrary.di

import android.content.Context
import com.example.mylibrary.data.local.AppDatabase
import com.example.mylibrary.data.repository.BookRepository
import com.example.mylibrary.data.repository.InMemorySettingsRepository
import com.example.mylibrary.data.repository.RoomBookRepository
import com.example.mylibrary.data.repository.SettingsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 * Contenedor de dependencias manual. Crea cada repositorio UNA sola vez
 * y se lo entrega a los ViewModels a través de [AppViewModelFactory].
 */
class AppContainer(context: Context) {

    private val appContext = context.applicationContext

    private val database: AppDatabase by lazy { AppDatabase.build(appContext) }
    private val roomBookRepository: RoomBookRepository by lazy { RoomBookRepository(database.bookDao()) }

    val bookRepository: BookRepository get() = roomBookRepository
    val settingsRepository: SettingsRepository = InMemorySettingsRepository()

    val viewModelFactory = AppViewModelFactory(this)

    init {
        // Primera vez que se abre la app: se cargan los libros de ejemplo.
        val prefs = appContext.getSharedPreferences("mylibrary", Context.MODE_PRIVATE)
        if (!prefs.getBoolean("seeded", false)) {
            CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
                roomBookRepository.seedSampleBooks()
                prefs.edit().putBoolean("seeded", true).apply()
            }
        }
    }
}
