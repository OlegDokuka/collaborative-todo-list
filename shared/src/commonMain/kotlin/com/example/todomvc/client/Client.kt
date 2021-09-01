package com.example.todomvc.client

import com.example.todomvc.model.Todo
import com.example.todomvc.model.TodoEvent

interface Client {
    fun handleTodos(handler: (TodoEvent) -> Unit)
    fun exchange(todo: List<Todo>)
    fun addTodo(todo: Todo)
    fun updateTodo(todo: Todo)
    fun removeTodo(todo: Todo)
}