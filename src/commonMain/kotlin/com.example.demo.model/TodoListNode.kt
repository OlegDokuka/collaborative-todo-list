package com.example.demo.model

import kotlinx.serialization.Serializable

@Serializable
data class TodoListNode (
    val version: Version = Version(),
    val parent: Version = Version(),
    val todoId: String
)