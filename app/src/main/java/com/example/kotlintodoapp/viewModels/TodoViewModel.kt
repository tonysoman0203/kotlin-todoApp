package com.example.kotlintodoapp.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlintodoapp.model.LoadingState
import com.example.kotlintodoapp.model.TodoItem
import com.example.kotlintodoapp.repositories.interfaces.TodoRepository
import kotlinx.coroutines.launch

class TodoViewModel(private val todoRepository: TodoRepository): ViewModel() {
    val data = todoRepository.getAllTodoItems()

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
                todoRepository.getAllTodoItems()
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
}