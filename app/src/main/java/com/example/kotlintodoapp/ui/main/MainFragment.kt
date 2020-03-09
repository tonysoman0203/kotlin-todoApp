package com.example.kotlintodoapp.ui.main

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlintodoapp.R
import com.example.kotlintodoapp.model.LoadingState
import com.example.kotlintodoapp.model.TodoItem
import com.example.kotlintodoapp.viewModels.TodoViewModel
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.main_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel by viewModel<TodoViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.data.observe(viewLifecycleOwner, Observer {
            todoItems -> val adapter = context?.let {
            TodoItemAdapter(it, todoItems)
        }
            todoList.layoutManager = LinearLayoutManager(context)
            todoList.adapter = adapter
        })
        viewModel.loadingState.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                LoadingState.Status.FAILED -> Toast.makeText(activity, it.msg, Toast.LENGTH_SHORT).show()
                LoadingState.Status.RUNNING -> Toast.makeText(activity, "Loading", Toast.LENGTH_SHORT).show()
                LoadingState.Status.SUCCESS -> Toast.makeText(activity, "Success", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = findNavController()
        btnAddTodo.setOnClickListener{
            navController.navigate(R.id.addTodoFragment2)
        }
    }
}

class TodoItemAdapter(val context: Context, private val todoItems: List<TodoItem>): RecyclerView.Adapter<TodoItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoItemAdapter.ViewHolder {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.item_todo,parent,false)
       return ViewHolder(view)
    }

    override fun getItemCount(): Int = todoItems.count()

    override fun onBindViewHolder(holder: TodoItemAdapter.ViewHolder, position: Int) {
        holder.bind(todoItems[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val todoRadio = itemView.findViewById<RadioButton>(R.id.todoRadio)
        fun bind(todoItem: TodoItem){
            todoRadio.text = todoItem.title
            todoRadio.buttonDrawable = itemView.resources.getDrawable(
                if(todoItem.isFinished)
                    android.R.drawable.radiobutton_on_background
                else
                    android.R.drawable.radiobutton_off_background)
        }
    }
}