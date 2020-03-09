package com.example.kotlintodoapp.repositories.interfaces

import androidx.lifecycle.LiveData
import com.example.kotlintodoapp.model.TodoItem

interface TodoRepository {
    fun add(newTodoItem: TodoItem)
    fun getAllTodoItems(): LiveData<List<TodoItem>>
}
