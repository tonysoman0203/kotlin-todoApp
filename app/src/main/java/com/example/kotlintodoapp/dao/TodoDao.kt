package com.example.kotlintodoapp.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.kotlintodoapp.model.TodoItem

@Dao
interface TodoDao {
    @Insert
    fun add(newTodoItem: TodoItem)

    @Query("SELECT * from todoItems")
    fun getAllTodoItems(): LiveData<List<TodoItem>>
}