package com.example.kotlintodoapp.repositories

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.example.kotlintodoapp.dao.TodoDao
import com.example.kotlintodoapp.model.TodoItem
import com.example.kotlintodoapp.repositories.interfaces.TodoRepository

class TodoRepositoryImpl(private val todoDao: TodoDao): TodoRepository {
    @WorkerThread
    override fun add(newTodoItem: TodoItem){
        todoDao.add(newTodoItem)
    }

    override fun getAllTodoItems(): LiveData<List<TodoItem>> {
        return todoDao.getAllTodoItems()
    }
}