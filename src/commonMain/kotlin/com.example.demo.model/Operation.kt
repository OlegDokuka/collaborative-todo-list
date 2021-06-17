package com.example.demo.model

import kotlinx.serialization.Serializable


@Serializable
sealed class Operation {
    abstract suspend fun apply(state: TodoListState)
}

@Serializable
data class AddTodo(val version: Version, val todo: Todo): Operation() {
    override suspend fun apply(state: TodoListState) {
        if (!state.exist(version)) {
            return
        }

        state.save(TodoListNode(version, state.version().next(), todo.id))

        val nodes = ArrayList<TodoNode>()

        var preVer = Version()
        var latestVer = Version(version.uid)
        for (c in todo.title) {
            latestVer = latestVer.next()
            nodes.add(TodoNode(todo.id, latestVer, preVer, c))
            preVer = latestVer
        }

        state.save(nodes)
    }
}

@Serializable
data class RemoveTodo(val version: Version): Operation() {
    override suspend fun apply(state: TodoListState) {
        state.delete(version)
    }
}

@Serializable
data class ExpandTodoTitle(val version: Version, val uuid: String, val titleChanges: String): Operation() {
    override suspend fun apply(state: TodoListState) {
        if (!state.exist(uuid, version)) {
            return
        }

        val nodes = ArrayList<TodoNode>()

        var preVer = version
        var latestVer = state.version(uuid)
        for (c in titleChanges) {
            latestVer = latestVer.next()
            nodes.add(TodoNode(uuid, latestVer, preVer, c))
            preVer = latestVer
        }

        state.save(nodes)
    }
}

@Serializable
data class ReduceTodoTitle(val version: Version, val uuid: String, val removed: Int): Operation() {
    override suspend fun apply(state: TodoListState) {
        val from: Int = version.number
        val to: Int = from + removed - 1

        state.delete(uuid, from..to)
    }
}

@Serializable
data class ToggleTodoStatus(val version: Version, val uuid: String, val complete: Boolean): Operation() {
    override suspend fun apply(state: TodoListState) {
        if (!state.exist(uuid, version)) {
            return
        }

        state.version(uuid)
        state.save(TodoNode(uuid, state.version(uuid), version.next(), null, complete))
    }
}
