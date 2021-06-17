package repository

import app.AppOptions
import com.example.demo.model.Todo
import com.example.demo.repository.TodoRepository
import kotlinx.browser.localStorage
import org.w3c.dom.get

class LocalStorageTodoRepository : TodoRepository {
    val quickStore: MutableMap<String, Todo> = mutableMapOf()

    override fun save(todo: Todo) {
        quickStore[todo.id] = todo

        localStorage.setItem(
            AppOptions.localStorageKey,
            JSON.stringify(quickStore.values.toTypedArray())
        )
    }

    override fun remove(todo: Todo) {
        quickStore.remove(todo.id)

        localStorage.setItem(
            AppOptions.localStorageKey,
            JSON.stringify(quickStore.values.toTypedArray())
        )
    }

    override fun update(todo: Todo) {
        quickStore[todo.id] = todo

        localStorage.setItem(
            AppOptions.localStorageKey,
            JSON.stringify(quickStore.values.toTypedArray())
        )
    }

    override fun all(): List<Todo> {
        val storedTodosJSON = localStorage[AppOptions.localStorageKey]

        return if (storedTodosJSON != null) {
            JSON.parse<Array<Todo>>(storedTodosJSON).map {
                Todo(it.id, it.title, it.completed)
            }.toList()
        } else {
            emptyList()
        }
    }

    override fun get(todoId: String): Todo? = quickStore[todoId]
}