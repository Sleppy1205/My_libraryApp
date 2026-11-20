package com.example.mylibrary

import android.app.Application
import com.example.mylibrary.di.AppContainer

class MyLibraryApplication : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
