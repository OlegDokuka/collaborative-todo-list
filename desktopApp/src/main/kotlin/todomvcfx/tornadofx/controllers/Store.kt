package todomvcfx.tornadofx.controllers

import com.example.todomvc.client.Client
import com.example.todomvc.client.RSocketClient
import com.example.todomvc.client.create
import com.example.todomvc.model.Todo
import com.example.todomvc.service.TodoService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.javafx.JavaFx
import kotlinx.coroutines.launch
import todomvcfx.tornadofx.models.FilterState
import todomvcfx.tornadofx.models.FilterState.Active
import todomvcfx.tornadofx.models.FilterState.Completed
import todomvcfx.tornadofx.models.TodoItem
import todomvcfx.tornadofx.repository.ProxyTodoRepository
import tornadofx.*

class Store : Controller() {
    val todos = SortedFilteredList<TodoItem>()
    val todoRepository = ProxyTodoRepository(todos)
    val todoService = TodoService(todoRepository)

    lateinit var client: Client

    init {
        MainScope().launch(Dispatchers.JavaFx) {
            client = RSocketClient.create()

            client.handleTodos {
                MainScope().launch(Dispatchers.JavaFx) {
                    todoService.handleEvent(it)
                }
            }
        }
    }

    fun addTodo(text: String) {
        val element = TodoItem(text = text)
        todos.add(element)
        client.addTodo(Todo(id = element.id.toString(), title = element.text))
    }

    fun removeTodo(todo: TodoItem) {
        todos.remove(todo)
        client.removeTodo(Todo(id = todo.id.toString(), title = todo.text, completed = todo.completed, removed = true))
    }

    fun updateTodo(todo: TodoItem) {
        client.updateTodo(Todo(id = todo.id.toString(), title = todo.text, completed = todo.completed))
    }

    fun toggleCompleted(completed: Boolean) = with(todos) {
        filter { it.completed != completed }.forEach { it.completed = completed }
        client.exchange(todoRepository.all())
        invalidate()
    }

    fun filterBy(state: FilterState) = when (state) {
        Completed -> todos.predicate = { it.completed }
        Active -> todos.predicate = { !it.completed }
        else -> todos.predicate = { true }
    }
}