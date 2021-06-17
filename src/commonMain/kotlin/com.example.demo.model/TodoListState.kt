package com.example.demo.model

interface TodoListState {
    suspend fun find(version: Version): TodoListNode
    suspend fun find(uid: String, version: Version): TodoNode

    suspend fun exist(version: Version): Boolean
    suspend fun exist(uid: String, version: Version): Boolean

    suspend fun version(): Version
    suspend fun version(uuid: String): Version

    suspend fun save(node: TodoListNode)
    suspend fun save(nodes: TodoNode)
    suspend fun save(nodes: Collection<TodoNode>)

    suspend fun delete(version: Version)
    suspend fun delete(uid: String, versions: IntRange)
}