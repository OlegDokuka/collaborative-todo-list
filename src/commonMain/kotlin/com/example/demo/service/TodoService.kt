package com.example.demo.service

import com.example.demo.model.EventType
import com.example.demo.model.Todo
import com.example.demo.model.TodoEvent
import com.example.demo.repository.TodoRepository

class TodoService(private val todoRepository: TodoRepository) {
    fun handleEvent(todoEvent: TodoEvent) {
        when (todoEvent.type) {
            EventType.ADD -> todoRepository.save(todoEvent.todo)
            EventType.UPDATE -> todoRepository.update(todoEvent.todo)
            EventType.REMOVE -> todoRepository.remove(todoEvent.todo)
            EventType.UPSERT -> when (check(todoEvent.todo)) {
                CheckResult.NEW -> todoRepository.save(todoEvent.todo)
                CheckResult.CHANGED -> todoRepository.update(todoEvent.todo)
                CheckResult.REMOVED -> todoRepository.remove(todoEvent.todo)
                else -> {
                }
            }
        }
    }

    fun listTodos(): List<Todo> = todoRepository.all()

    private fun check(todo: Todo): CheckResult {
        val existingTodo = todoRepository.get(todo.id)

        if (existingTodo != null) {
            return if (existingTodo == todo) {
                CheckResult.SAME
            } else if (existingTodo.removed || todo.removed) {
                CheckResult.REMOVED
            } else {
                CheckResult.CHANGED
            }
        }

        return if (todo.removed) CheckResult.REMOVED else CheckResult.NEW

    }

    enum class CheckResult {
        SAME, CHANGED, REMOVED, NEW
    }
}