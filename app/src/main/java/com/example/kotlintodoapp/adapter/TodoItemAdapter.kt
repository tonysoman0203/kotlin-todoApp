package com.example.kotlintodoapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlintodoapp.R
import com.example.kotlintodoapp.model.TodoItem

class TodoItemAdapter(private val todoItems: List<TodoItem>): RecyclerView.Adapter<TodoItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_todo,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = todoItems.count()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
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