package com.example.mylibrary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylibrary.navigation.AppNavigation
import com.example.mylibrary.ui.screens.SettingsViewModel
import com.example.mylibrary.ui.theme.MyLibraryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val factory = (application as MyLibraryApplication).container.viewModelFactory
        setContent {
            val settingsViewModel: SettingsViewModel = viewModel(factory = factory.settings)
            val settings by settingsViewModel.uiState.collectAsStateWithLifecycle()

            MyLibraryTheme(darkTheme = settings.darkTheme) {
                Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    AppNavigation(factory = factory)
                }
            }
        }
    }
}
