package com.example.todomvc.repository

import com.example.todomvc.model.Todo

interface TodoRepository {
    fun save(todo: Todo)
    fun remove(todo: Todo)
    fun update(todo: Todo)
    fun all(): List<Todo>
    fun get(todoId: String): Todo?
}