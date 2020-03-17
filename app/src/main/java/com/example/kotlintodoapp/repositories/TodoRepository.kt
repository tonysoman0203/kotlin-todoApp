package com.example.kotlintodoapp.repositories

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.kotlintodoapp.dao.TodoDao
import com.example.kotlintodoapp.model.TodoItem
import com.example.kotlintodoapp.repositories.interfaces.BaseTodoRepository

class TodoRepository (private val todoDao: TodoDao): BaseTodoRepository {
    @WorkerThread
    override fun add(newTodoItem: TodoItem){
        todoDao.add(newTodoItem)
    }

    override fun getAllTodoItems(): LiveData<List<TodoItem>> {
        return todoDao.getAllTodoItems()
    }

    @WorkerThread
    override fun update(update: TodoItem) {
        todoDao.update(update)
    }

    @WorkerThread
    override fun remove(id: Int) {
       todoDao.remove(id)
    }
}