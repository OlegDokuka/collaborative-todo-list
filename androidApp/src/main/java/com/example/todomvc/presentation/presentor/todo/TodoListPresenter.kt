package com.example.todomvc.presentation.presentor.todo

import android.os.Handler
import android.os.Looper
import com.example.todomvc.client.Client
import com.example.todomvc.client.RSocketClient
import com.example.todomvc.client.create
import com.example.todomvc.model.Todo
import com.example.todomvc.presentation.BasePresenter
import com.example.todomvc.service.TodoService
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class TodoListPresenter : BasePresenter<TodoListContract.View>(), TodoListContract.Interaction {

    private val repository = SimpleTodoRepository()
    private val service = TodoService(repository)
    private lateinit var client: Client

    init {
        MainScope().launch {
            client = RSocketClient.create()
            client.handleTodos {
                Handler(Looper.getMainLooper()).post {
                    service.handleEvent(it)
                    mView.showChangeItem(repository.all().toMutableList())
                }
            }
        }
    }

    override fun add(todoTitle: String) {
        val todo = Todo(title = todoTitle)
        repository.save(todo)
        client.addTodo(todo)
        mView.showChangeItem(repository.all().toMutableList())
    }

    override fun update(todo: Todo) {
        repository.update(todo)
        client.updateTodo(todo)
        mView.showChangeItem(repository.all().toMutableList())
    }

    override fun delete(todo: Todo) {
        repository.remove(todo)
        client.removeTodo(todo)
        mView.showChangeItem(repository.all().toMutableList())
    }

    override fun showAll() {
        mView.showChangeItem(repository.all().toMutableList())
    }

    override fun showActive() {
        mView.showChangeItem(repository.all().filter { !it.completed }.toMutableList())
    }

    override fun showCompleted() {
        mView.showChangeItem(repository.all().filter { it.completed }.toMutableList())
    }

}