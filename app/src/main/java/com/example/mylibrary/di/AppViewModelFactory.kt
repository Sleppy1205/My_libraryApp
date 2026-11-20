package com.example.mylibrary.di

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mylibrary.ui.screens.BookDetailViewModel
import com.example.mylibrary.ui.screens.BookFormViewModel
import com.example.mylibrary.ui.screens.LibraryViewModel
import com.example.mylibrary.ui.screens.LoginViewModel
import com.example.mylibrary.ui.screens.RegisterViewModel
import com.example.mylibrary.ui.screens.SettingsViewModel

/**
 * Fábricas de ViewModel (ViewModelProvider.Factory). Los ViewModels nunca crean
 * sus repositorios: los reciben ya construidos desde el [AppContainer].
 */
class AppViewModelFactory(private val container: AppContainer) {

    val library: ViewModelProvider.Factory = viewModelFactory {
        initializer { LibraryViewModel(container.bookRepository, container.settingsRepository) }
    }

    val settings: ViewModelProvider.Factory = viewModelFactory {
        initializer { SettingsViewModel(container.settingsRepository) }
    }

    val login: ViewModelProvider.Factory = viewModelFactory {
        initializer { LoginViewModel(container.settingsRepository) }
    }

    val register: ViewModelProvider.Factory = viewModelFactory {
        initializer { RegisterViewModel(container.settingsRepository) }
    }

    fun detail(bookId: Int): ViewModelProvider.Factory = viewModelFactory {
        initializer { BookDetailViewModel(bookId, container.bookRepository) }
    }

    fun form(bookId: Int): ViewModelProvider.Factory = viewModelFactory {
        initializer { BookFormViewModel(bookId, container.bookRepository) }
    }
}
