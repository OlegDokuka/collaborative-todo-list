package reducers

import actions.SetTodoFilter
import com.example.demo.model.TodoFilter
import redux.RAction

fun visibilityFilter(
    state: TodoFilter = TodoFilter.ANY,
    action: RAction
): TodoFilter = when (action) {
    is SetTodoFilter -> action.filter
    else -> state
}