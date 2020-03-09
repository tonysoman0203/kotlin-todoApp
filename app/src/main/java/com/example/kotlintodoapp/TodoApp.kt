package com.example.kotlintodoapp

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class TodoApp: Application() {
    val _modules = listOf(viewModelModule, repositoryModule, databaseModule)

    override fun onCreate() {
        super.onCreate()
        // Start Koin
        startKoin{
            androidLogger()
            androidContext(this@TodoApp)
            modules(_modules)
        }
    }
}