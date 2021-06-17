package com.example.demo.ui.components

import kotlinx.html.InputType
import kotlinx.html.js.onBlurFunction
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.onKeyUpFunction
import com.example.demo.model.Todo
import org.w3c.dom.events.Event
import react.*
import react.dom.*
import com.example.demo.utils.Keys
import com.example.demo.utils.value

@JsExport
class TodoItem : RComponent<TodoItemProps, TodoItemState>() {

    override fun componentWillMount() {
        setState {
            editText = ""
        }
    }

    override fun componentWillReceiveProps(nextProps: TodoItemProps) {
        state.editText = nextProps.todo.title
    }

    override fun RBuilder.render() {
        div(classes = "view") {

            input(classes = "toggle", type = InputType.checkBox) {

                attrs.onChangeFunction = { event ->
                    val c = event.currentTarget.asDynamic().checked as Boolean
                    props.updateTodo(props.todo.title, c)
                }

                ref { it?.checked = props.todo.completed }
            }
            label {
                +props.todo.title
            }
            button(classes = "destroy") {
                attrs.onClickFunction = {
                    props.removeTodo()
                }
            }
        }
        input(classes = "edit", type = InputType.text) {
            attrs {
                value = state.editText
                onChangeFunction = { event ->
                    val text = event.value
                    setState {
                        editText = text
                    }
                }
                onBlurFunction = { finishEditing(state.editText) }
                onKeyUpFunction = ::handleKeyUp

            }

            if (props.editing) {
                ref { it?.focus() }
            }
        }
    }

    private fun finishEditing(title: String) {
        if (title.isNotBlank()) {
            props.updateTodo(title, props.todo.completed)
        } else {
            props.removeTodo()
        }

        props.endEditing()
    }

    private fun handleKeyUp(keyEvent: Event) {
        val key = Keys.fromString(keyEvent.asDynamic().key as String)
        when (key) {
            Keys.Enter -> {
                finishEditing(state.editText)
            }
            Keys.Escape -> {
                props.endEditing()
            }
        }

    }

}

external interface TodoItemProps : RProps {
    var todo: Todo
    var editing: Boolean
    var removeTodo: () -> Unit
    var updateTodo: (String, Boolean) -> Unit
    var endEditing: () -> Unit
}

external interface TodoItemState : RState {
    var editText: String
    var checked: Boolean
}

fun RBuilder.todoItem(
    todo: Todo,
    editing: Boolean,
    removeTodo: () -> Unit,
    updateTodo: (String, Boolean) -> Unit,
    endEditing: () -> Unit
) = child(TodoItem::class) {
    attrs.todo = todo
    attrs.editing = editing
    attrs.removeTodo = removeTodo
    attrs.updateTodo = updateTodo
    attrs.endEditing = endEditing
}