package reducers

import com.example.demo.model.Todo
import actions.AddTodo
import actions.ToggleTodo
import redux.RAction

fun todos(state: Array<Todo> = emptyArray(), action: RAction): Array<Todo> = when (action) {
    is AddTodo -> state + Todo(action.uid, action.text, false)
    is ToggleTodo -> state.map {
        if (it.id == action.uid) {
            it.copy(completed = !it.completed)
        } else {
            it
        }
    }.toTypedArray()
    else -> state
}