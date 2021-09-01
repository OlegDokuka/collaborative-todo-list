package com.example.todomvc.repository

import com.example.todomvc.model.Todo
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

@Repository
class InMemoryTodoRepository : TodoRepository {

    private val store: ConcurrentMap<String, Todo> = ConcurrentHashMap()

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