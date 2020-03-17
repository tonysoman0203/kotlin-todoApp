package com.example.kotlintodoapp.repositories.interfaces

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.kotlintodoapp.model.TodoItem

interface BaseTodoRepository {
    fun add(newTodoItem: TodoItem)
    fun getAllTodoItems(): LiveData<List<TodoItem>>
    fun update(update: TodoItem);
    fun remove(id: Int)
}
