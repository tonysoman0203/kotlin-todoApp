package com.example.kotlintodoapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlintodoapp.helper.TodoLogger
import com.example.kotlintodoapp.model.LoadingState
import com.example.kotlintodoapp.model.TodoItem
import com.example.kotlintodoapp.repositories.interfaces.BaseTodoRepository
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.lang.Thread.sleep

class TodoViewModel(private val todoRepository: BaseTodoRepository): ViewModel() {
    var data =  todoRepository.getAllTodoItems()

    private val _loadingState = MutableLiveData<LoadingState>()
    val loadingState: LiveData<LoadingState>
        get() = _loadingState

    init {
        fetchData()
    }

    fun fetchData() {
        viewModelScope.launch {
            try {
                _loadingState.value = LoadingState.LOADING
                data = todoRepository.getAllTodoItems()
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
        try{
            _loadingState.value = LoadingState.LOADING
            todoItem.isFinished = isFinished
            todoRepository.update(todoItem)
            _loadingState.value = LoadingState.LOADED
        }catch (e: Exception){
            _loadingState.value = LoadingState.error(e.message)
        }
    }

    fun removeTodo(oldTodoItem: TodoItem){
        try{
            _loadingState.value = LoadingState.LOADING
            todoRepository.remove(oldTodoItem.itemId)
            _loadingState.value = LoadingState.LOADED
        }catch (e: Exception){
            _loadingState.value = LoadingState.error(e.message)
        }
    }

    fun clear(){
        viewModelScope.coroutineContext.cancel()
    }
}