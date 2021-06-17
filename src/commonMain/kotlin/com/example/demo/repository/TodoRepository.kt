package com.example.demo.repository

import com.example.demo.model.Todo

interface TodoRepository {
    fun save(todo: Todo)
    fun remove(todo: Todo)
    fun update(todo: Todo)
    fun all(): List<Todo>
    fun get(todoId: String): Todo?
}
