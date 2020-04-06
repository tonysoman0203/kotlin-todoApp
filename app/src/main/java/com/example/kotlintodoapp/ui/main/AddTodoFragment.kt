package com.example.kotlintodoapp.ui.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.kotlintodoapp.R
import com.example.kotlintodoapp.model.TodoItem
import com.example.kotlintodoapp.viewModels.TodoViewModel
import kotlinx.android.synthetic.main.fragment_add_todo.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddTodoFragment : Fragment() {

    private val viewModel by viewModel<TodoViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_todo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        txtAddTodoHeader.text = getText(R.string.add_todo_1)
        btnAddTodoConfirm.text = getText(R.string.add_todo_confirm)
        btnAddTodoReset.text = getText(R.string.add_todo_reset)

        btnAddTodoConfirm.setOnClickListener {
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
            val newTodoItem =
                TodoItem(body = edtBody.text.toString(), title = edtTitle.text.toString())
            viewModel.addTodo(newTodoItem)
            findNavController().popBackStack()
        }

        btnAddTodoReset.setOnClickListener {
            edtBody.text = null
            edtTitle.text = null
        }
    }
}
