package com.example.mylibrary.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mylibrary.data.repository.SettingsRepository
import com.example.mylibrary.model.AppSettings
import com.example.mylibrary.model.SortOrder
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(private val repository: SettingsRepository) : ViewModel() {

    val uiState: StateFlow<AppSettings> = repository.settings

    fun onDarkThemeChange(enabled: Boolean) {
        viewModelScope.launch { repository.setDarkTheme(enabled) }
    }

    fun onSortOrderChange(order: SortOrder) {
        viewModelScope.launch { repository.setSortOrder(order) }
    }

    fun onLanguageChange(language: String) {
        viewModelScope.launch { repository.setLanguage(language) }
    }
}
