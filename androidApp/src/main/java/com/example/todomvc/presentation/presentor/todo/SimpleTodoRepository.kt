package com.example.todomvc.presentation.presentor.todo

import com.example.todomvc.model.Todo
import com.example.todomvc.repository.TodoRepository

class SimpleTodoRepository : TodoRepository {
    private val store: HashMap<String, Todo> = HashMap()

    override fun save(todo: Todo) {
        store[todo.id] = todo
    }

    override fun remove(todo: Todo) {
        store.remove(todo.id)
    }

    override fun update(todo: Todo) {
        store[todo.id] = todo
    }

    override fun all(): List<Todo> = store.values.toList()

    override fun get(todoId: String): Todo? = store[todoId]
}