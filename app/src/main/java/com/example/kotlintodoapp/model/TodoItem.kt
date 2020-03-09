package com.example.kotlintodoapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todoItems")
data class TodoItem(
    @PrimaryKey(autoGenerate = true) val itemId: Int = 0,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "body") var body: String,
    @ColumnInfo(name = "finished") var isFinished: Boolean = false
) {
    override fun toString(): String {
        return "TodoItem(itemId=$itemId, title='$title', body='$body', isFinished=$isFinished)"
    }
}

