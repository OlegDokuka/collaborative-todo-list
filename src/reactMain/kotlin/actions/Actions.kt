package actions

import com.benasher44.uuid.uuid4
import com.example.demo.model.TodoFilter
import redux.RAction

class SetTodoFilter(val filter: TodoFilter) : RAction

class AddTodo(val uid: String = uuid4().toString(), val text: String): RAction

class EditTodo(val uid: String, val text: String): RAction

class ToggleTodo(val uid: String): RAction

