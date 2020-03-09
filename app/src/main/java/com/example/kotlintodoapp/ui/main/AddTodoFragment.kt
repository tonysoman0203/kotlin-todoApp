package com.example.kotlintodoapp.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kotlintodoapp.R
import com.example.kotlintodoapp.model.TodoItem
import com.example.kotlintodoapp.viewModels.TodoViewModel
import kotlinx.android.synthetic.main.fragment_add_todo.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddTodoFragment : Fragment() {
    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel by viewModel<TodoViewModel>()
    private var title: String = ""
    private var body: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_todo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        txtAddTodoHeader.text = getText(R.string.add_todo_1)
        btnAddTodoConfirm.text = getText(R.string.add_todo_confirm)
        btnAddTodoReset.text = getText(R.string.add_todo_reset)

        btnAddTodoConfirm.setOnClickListener {
            val newTodoItem = TodoItem(body = edtBody.text.toString(), title = edtTitle.text.toString())
            viewModel.addTodo(newTodoItem)
        }
    }
}
