package com.example.todomvc.presentation.presentor.todo

import com.example.todomvc.model.Todo
import com.example.todomvc.presentation.BaseView

interface TodoListContract {
    interface View : BaseView {

        fun showChangeItem(list : MutableList<Todo>)

    }

    interface Interaction {
        fun add(todoTitle: String)
        fun update(todo: Todo)
        fun delete(todo: Todo)
        fun showAll()
        fun showActive()
        fun showCompleted()


    }
}