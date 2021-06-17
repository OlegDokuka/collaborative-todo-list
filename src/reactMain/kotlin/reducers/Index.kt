package reducers

import com.example.demo.model.Todo
import com.example.demo.model.TodoFilter
import redux.Reducer
import redux.combineReducers
import kotlin.reflect.KProperty1


data class State(
    val todos: Array<Todo> = emptyArray(),
    val visibilityFilter: TodoFilter = TodoFilter.ANY
)

fun <S, A, R> combineReducers(reducers: Map<KProperty1<S, R>, Reducer<*, A>>): Reducer<S, A> {
    return combineReducers(reducers.mapKeys { it.key.name })
}

fun combinedReducers() = combineReducers(
    mapOf(
        State::todos to ::todos,
        State::visibilityFilter to ::visibilityFilter
    )
)
