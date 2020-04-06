package com.example.kotlintodoapp.drivers

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.kotlintodoapp.dao.TodoDao
import com.example.kotlintodoapp.model.TodoItem

@Database(entities = [TodoItem::class], version = 1, exportSchema = false)
abstract class TodoRoomDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
}