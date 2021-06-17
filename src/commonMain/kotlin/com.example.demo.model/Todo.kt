package com.example.demo.model

import com.benasher44.uuid.uuid4
import kotlinx.serialization.Serializable

@Serializable
data class Todo (
    val id: String = uuid4().toString(),
    val title: String,
    var completed: Boolean = false
)

enum class TodoFilter {
    ANY, COMPLETED, PENDING;

    fun filter(todo: Todo): Boolean {
        return when (this) {
            ANY -> true
            COMPLETED -> todo.completed
            PENDING -> !todo.completed
        }
    }
}