package com.example.mylibrary.data.repository

import com.example.mylibrary.model.AppSettings
import com.example.mylibrary.model.SortOrder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/** Contrato de preferencias y sesión. En la siguiente etapa se implementa con DataStore. */
interface SettingsRepository {
    val settings: StateFlow<AppSettings>
    suspend fun setDarkTheme(enabled: Boolean)
    suspend fun setSortOrder(order: SortOrder)
    suspend fun setLanguage(language: String)
    suspend fun setUser(name: String, email: String)
}

/** Implementación temporal en memoria. */
class InMemorySettingsRepository : SettingsRepository {
    private val state = MutableStateFlow(AppSettings())
    override val settings: StateFlow<AppSettings> = state.asStateFlow()

    override suspend fun setDarkTheme(enabled: Boolean) {
        state.update { it.copy(darkTheme = enabled) }
    }

    override suspend fun setSortOrder(order: SortOrder) {
        state.update { it.copy(sortOrder = order) }
    }

    override suspend fun setLanguage(language: String) {
        state.update { it.copy(language = language) }
    }

    override suspend fun setUser(name: String, email: String) {
        state.update { it.copy(userName = name, userEmail = email) }
    }
}
