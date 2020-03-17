package com.example.kotlintodoapp.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlintodoapp.helper.TodoLogger
import com.example.kotlintodoapp.model.LoadingState
import com.example.kotlintodoapp.model.TodoItem
import com.example.kotlintodoapp.repositories.interfaces.BaseTodoRepository
import kotlinx.coroutines.launch
import java.util.logging.Logger

class TodoViewModel(private val todoRepository: BaseTodoRepository): ViewModel() {
    var data =  todoRepository.getAllTodoItems()

    private val _loadingState = MutableLiveData<LoadingState>()
    val loadingState: LiveData<LoadingState>
        get() = _loadingState

    init {
        fetchData()
    }


    private fun fetchData(){
        viewModelScope.launch {
            try {
                _loadingState.value = LoadingState.LOADING
                TodoLogger.debug(message = "${todoRepository.getAllTodoItems().value}")
                data = todoRepository.getAllTodoItems()
                TodoLogger.debug(message = "${data.value}")
                _loadingState.value = LoadingState.LOADED
            } catch (e: Exception) {
                _loadingState.value = LoadingState.error(e.message)
            }
        }
    }

    fun addTodo(newTodoItem: TodoItem){
        try{
            _loadingState.value = LoadingState.LOADING
            todoRepository.add(newTodoItem)
            _loadingState.value = LoadingState.LOADED
        }catch (e: Exception) {
            _loadingState.value = LoadingState.error(e.message)
        }
    }

    fun setTodoItemIsFinished(isFinished: Boolean, todoItem: TodoItem){
        TodoLogger.debug(tag = TodoViewModel::javaClass.name , message = "isFinished : $isFinished")
        todoItem.isFinished = isFinished
        todoRepository.update(todoItem)
    }

    fun removeTodo(oldTodoItem: TodoItem){
        todoRepository.remove(oldTodoItem.itemId)
    }
}