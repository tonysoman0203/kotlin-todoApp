package com.example.kotlintodoapp.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlintodoapp.R
import com.example.kotlintodoapp.adapter.TodoItemAdapter
import com.example.kotlintodoapp.helper.TodoLogger
import com.example.kotlintodoapp.model.LoadingState
import com.example.kotlintodoapp.model.TodoItem
import com.example.kotlintodoapp.viewModels.TodoViewModel
import kotlinx.android.synthetic.main.main_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment() {
    private val viewModel by viewModel<TodoViewModel>()
    private var adapter: TodoItemAdapter ?= null;

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        TodoLogger.debug(MainFragment::javaClass.name, "onViewCreated")

        val onCheckBoxClicked: (isChecked: Boolean, todoItem: TodoItem) -> Unit = {
                isChecked: Boolean, todoItem: TodoItem ->
            viewModel.setTodoItemIsFinished(isChecked, todoItem);
            adapter?.notifyDataSetChanged()
        }

        val onLongPressedItem: (position: Int, todoItem: TodoItem) -> Unit = {
                position: Int, todoItem ->
            viewModel.removeTodo(todoItem)
            adapter?.notifyItemRemoved(position)
        }

        adapter = TodoItemAdapter(onCheckBoxClicked, onLongPressedItem);
        todoList.apply {
            layoutManager = LinearLayoutManager(
                context
            )
        }
        todoList.adapter = adapter;

        viewModel.data.observe(viewLifecycleOwner, Observer {
            TodoLogger.debug(message = "${it}")
            adapter?.todoItems = it ?: emptyList()
        })

        viewModel.loadingState.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                LoadingState.Status.FAILED -> Toast.makeText(activity, it.msg, Toast.LENGTH_SHORT).show()
                LoadingState.Status.RUNNING -> Toast.makeText(activity, "Loading", Toast.LENGTH_SHORT).show()
                LoadingState.Status.SUCCESS -> Toast.makeText(activity, "Success", Toast.LENGTH_SHORT).show()
            }
        })

        val navController = findNavController()
        btnAddTodo.setOnClickListener{
            navController.navigate(R.id.addTodoFragment2, null, navOptions {
                popUpTo = R.id.addTodoFragment2
                launchSingleTop = true
            })
        }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        TodoLogger.debug(MainFragment::javaClass.name, "onActivityCreated")
    }

}