package com.example.kotlintodoapp.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlintodoapp.R
import com.example.kotlintodoapp.helper.TodoLogger
import com.example.kotlintodoapp.model.TodoItem
import kotlinx.android.synthetic.main.item_todo.view.*


class TodoItemAdapter(
    private val onCheckBoxClicked: (isChecked: Boolean, todoItem: TodoItem) -> Unit,
    private val onLongPressedItem: (position: Int, todoItem: TodoItem) -> Unit
): RecyclerView.Adapter<TodoItemAdapter.TodoItemViewHolder>() {
    var todoItems: List<TodoItem> = emptyList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_todo,parent,false)
        val viewHolder =  TodoItemViewHolder(view);
        viewHolder.itemView.chkTodo.setOnClickListener{
            if(viewHolder.adapterPosition != RecyclerView.NO_POSITION){
                val isFinishedFromDb = this.todoItems[viewHolder.adapterPosition].isFinished;
                onCheckBoxClicked.invoke(!isFinishedFromDb, this.todoItems[viewHolder.adapterPosition]);
            }
        };
        viewHolder.itemView.setOnLongClickListener{
            onLongPressedItem.invoke(viewHolder.adapterPosition, this.todoItems[viewHolder.adapterPosition])
            return@setOnLongClickListener true
        }

        return viewHolder
    }

    override fun getItemCount(): Int = this.todoItems.count()

    override fun onBindViewHolder(holder: TodoItemViewHolder, position: Int) {
        holder.bind(this.todoItems[position])
    }

    inner class TodoItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(todoItem: TodoItem){
            TodoLogger.debug(message = "item bound $itemView")

            itemView.chkTodo.isChecked = todoItem.isFinished
            itemView.txtTodo.text = todoItem.title
            itemView.txtTodo.paintFlags = if (todoItem.isFinished)
                itemView.txtTodo.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            else
                itemView.txtTodo.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
    }
}