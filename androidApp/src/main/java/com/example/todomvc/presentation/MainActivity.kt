package com.example.todomvc.presentation

import android.os.Bundle
import com.example.todomvc.androidApp.R
import com.example.todomvc.presentation.presentor.todo.TodoListFragment

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.container)
        replaceFragment(TodoListFragment(), R.id.container)
    }
}
