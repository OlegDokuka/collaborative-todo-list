package app

import com.benasher44.uuid.uuid4
import com.example.demo.model.*
import components.headerInput
import components.info
import components.todoBar
import components.todoList
import io.ktor.utils.io.core.*
import io.rsocket.kotlin.ExperimentalMetadataApi
import io.rsocket.kotlin.RSocket
import io.rsocket.kotlin.metadata.CompositeMetadata
import io.rsocket.kotlin.metadata.RoutingMetadata
import io.rsocket.kotlin.metadata.metadata
import io.rsocket.kotlin.payload.buildPayload
import io.rsocket.kotlin.payload.data
import kotlinx.browser.document
import kotlinx.browser.localStorage
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.html.InputType
import kotlinx.html.id
import kotlinx.html.js.onChangeFunction
import kotlinx.html.title
import kotlinx.serialization.json.*
import kotlinx.serialization.*
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.get
import react.*
import react.dom.attrs
import react.dom.input
import react.dom.label
import react.dom.section
import utils.translate

object AppOptions {
    var language = "no-language"
    var localStorageKey = "todos-koltin-react"
}


@JsExport
@ExperimentalMetadataApi
class App(props: AppProps) : RComponent<AppProps, AppState>(props) {

    override fun AppState.init(props: AppProps) {
        console.log("componentWillReceiveProps $props")
        props.rsocket
            .requestStream(buildPayload {
                data(ByteReadPacket.Empty)
                metadata(CompositeMetadata(RoutingMetadata("todos")))
            })
            .onEach {
                val string = it.data.readText()
                console.log("received json $string")
                val todo = Json.decodeFromString<Operation>(string)
                console.log(todo)
//                storeTodos(state.todos + todo)
//
//                setState {
//                    todos = state.todos + todo
//                }
            }
            .launchIn(MainScope())
    }

    override fun componentWillMount() {
        console.log("component will mount app")
        setState {
            todos = loadTodos()
        }
    }

    override fun RBuilder.render() {

        val currentFilter = when (props.route) {
            "pending" -> TodoFilter.PENDING
            "completed" -> TodoFilter.COMPLETED
            else -> TodoFilter.ANY
        }

        section("todoapp") {
            headerInput(::createTodo)


            if (state.todos.isNotEmpty()) {

                val allChecked = isAllCompleted()

                section("main") {
                    input(InputType.checkBox, classes = "toggle-all") {
                        attrs {
                            id = "toggle-all"
                            checked = allChecked

                            onChangeFunction = {event ->
                                val isChecked = (event.currentTarget as HTMLInputElement).checked

                                setAllStatus(isChecked)
                            }
                        }
                    }
                    label {
                        attrs["htmlFor"] = "toggle-all"
                        attrs.title = "Mark all as complete".translate()
                    }

                    todoList(::removeTodo, ::updateTodo, state.todos, currentFilter)
                }

                todoBar(pendingCount = countPending(),
                        anyCompleted = state.todos.any { todo -> todo.completed },
                        clearCompleted = ::clearCompleted,
                        currentFilter = currentFilter,
                        updateFilter = ::updateFilter)
            }

        }
        info()
    }


    private fun loadTodos(): List<Todo> {
        val storedTodosJSON = localStorage[AppOptions.localStorageKey]

        return if (storedTodosJSON != null) {
            JSON.parse<Array<Todo>>(storedTodosJSON).map {
                Todo(it.id, it.title, it.completed)
            }.toList()
        } else {
            emptyList()
        }
    }

    private fun updateFilter(newFilter: TodoFilter) {
        document.location!!.href = "#?route=${newFilter.name.toLowerCase()}"
    }

    private fun countPending() = pendingTodos().size

    private fun removeTodo(todo: Todo) {
        console.log("removeTodo [${todo.id}] ${todo.title}")
        saveTodos(state.todos - todo)
    }

    private fun createTodo(todo: Todo) {
        console.log("createTodo [${todo.id}] ${todo.title}")

        MainScope().launch {
            props.rsocket
                .fireAndForget(buildPayload {
                    data(Json.encodeToString(AddTodo(Version(uuid4().toString()), todo)))
                    metadata(CompositeMetadata(RoutingMetadata("todos.add")))
                })
        }
//        props.rsocket.dispatch(AddTodo(uid = todo.id, text = todo.title))

        saveTodos(state.todos + todo)
        state
    }

    private fun updateTodo(todo: Todo) {
        console.log("updateTodo [${todo.id}] ${todo.title}")

        val newTodos = state.todos.map { oldTodo ->
            if (todo.id == oldTodo.id) {
                todo
            } else {
                oldTodo
            }
        }
        saveTodos(newTodos)
    }

    private fun setAllStatus(newStatus: Boolean) {
        saveTodos(state.todos.map { todo -> todo.copy(completed = newStatus) })
    }

    private fun saveTodos(updatedTodos: List<Todo>) {
        console.log("saving: ${updatedTodos.toTypedArray()}")

        storeTodos(updatedTodos)

        setState {
            todos = updatedTodos
        }
    }

    private fun storeTodos(todos: List<Todo>) {
        localStorage.setItem(AppOptions.localStorageKey, JSON.stringify(todos.toTypedArray()))
    }

    private fun clearCompleted() {
        saveTodos(pendingTodos())
    }

    private fun isAllCompleted(): Boolean {
        return state.todos.fold(true) { allCompleted, todo ->
            allCompleted && todo.completed
        }
    }

    private fun pendingTodos() : List<Todo> {
        return state.todos.filter { todo -> !todo.completed }
    }
}


external interface AppState:  RState {
    var todos: List<Todo>
}

external interface AppProps : RProps {
    var route: String
    var rsocket: RSocket
}

//external interface AppStateProps:  RProps {
//    var todos: Array<Todo>
//}
//
//val appContainer = rConnect<State, RAction, WrapperAction, RProps, AppStateProps, RProps, AppProps>(
//    { state, _ ->
//        todos = state.todos
//    },
//    { dispatch, _ -> }
//)(App::class.js.unsafeCast<RClass<AppProps>>())

fun RBuilder.app(route: String, rsocket: RSocket) = child(App::class) {
    attrs.route = route
    attrs.rsocket = rsocket
}
