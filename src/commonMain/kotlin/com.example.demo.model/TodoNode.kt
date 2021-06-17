package com.example.demo.model

import com.benasher44.uuid.uuid4
import kotlinx.serialization.Serializable

@Serializable
data class TodoNode (
    val id: String = uuid4().toString(),
    val version: Version = Version(),
    val parent: Version = Version(),
    val change: Char? = null,
    val completed: Boolean? = null,
    val tombstone: Boolean = false
)