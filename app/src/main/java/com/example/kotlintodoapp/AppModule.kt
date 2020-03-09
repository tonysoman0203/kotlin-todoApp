package com.example.kotlintodoapp

import android.app.Application
import androidx.room.Room
import com.example.kotlintodoapp.dao.TodoDao
import com.example.kotlintodoapp.drivers.TodoRoomDatabase
import com.example.kotlintodoapp.repositories.TodoRepositoryImpl
import com.example.kotlintodoapp.repositories.interfaces.TodoRepository
import com.example.kotlintodoapp.viewModels.TodoViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val databaseModule = module {

    fun provideDatabase(application: Application): TodoRoomDatabase {
        return Room.databaseBuilder(application, TodoRoomDatabase::class.java, "todos_db")
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }


    fun provideDao(database: TodoRoomDatabase): TodoDao {
        return database.todoDao()
    }

    single { provideDatabase(androidApplication()) }
    single { provideDao(get()) }
}

val repositoryModule = module {
    fun provideUserRepository(dao: TodoDao): TodoRepository {
        return TodoRepositoryImpl(dao)
    }

    single { provideUserRepository(get()) }
}

val viewModelModule = module {
    viewModel { TodoViewModel(get()) }
}