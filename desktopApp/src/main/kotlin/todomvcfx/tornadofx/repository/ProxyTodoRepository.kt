package todomvcfx.tornadofx.repository

import com.benasher44.uuid.uuidFrom
import com.example.todomvc.model.Todo
import com.example.todomvc.repository.TodoRepository
import todomvcfx.tornadofx.models.TodoItem
import tornadofx.SortedFilteredList

class ProxyTodoRepository(val list: SortedFilteredList<TodoItem>) : TodoRepository {

    override fun save(todo: Todo) {
        this.list.add(TodoItem(id = uuidFrom(todo.id), text = todo.title))
    }

    override fun remove(todo: Todo) {
        this.list.remove(TodoItem(id = uuidFrom(todo.id), text = todo.title, completed = todo.completed))
    }

    override fun update(todo: Todo) {
        val id = uuidFrom(todo.id)
        this.list.forEach {
            if (it.id == id) {
                it.completed = todo.completed
                it.textProperty.set(todo.title)
                it.completedProperty.set(todo.completed)
            }
        }
    }

    override fun all(): List<Todo> = list.map { Todo(id = it.id.toString(), title = it.text, completed = it.completed) }

    override fun get(todoId: String): Todo? {
        val id = uuidFrom(todoId)
        return this.list.firstOrNull { it.id == id }?.let { Todo(id = it.id.toString(), title = it.text, completed = it.completed) }
    }
}