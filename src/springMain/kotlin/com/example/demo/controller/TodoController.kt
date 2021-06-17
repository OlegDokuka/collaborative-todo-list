package com.example.demo.controller

import com.benasher44.uuid.Uuid
import com.benasher44.uuid.uuid4
import com.example.demo.model.AddTodo
import com.example.demo.model.Operation
import com.example.demo.model.Todo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.rsocket.RSocketRequester
import org.springframework.messaging.rsocket.annotation.ConnectMapping
import org.springframework.stereotype.Controller
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap


@Controller
class TodoController {

    val clients: ConcurrentMap<Uuid, RSocketRequester> = ConcurrentHashMap()
    val streams: ConcurrentMap<RSocketRequester, MutableSharedFlow<Todo>> = ConcurrentHashMap()

    @ConnectMapping("")
    fun handleCollaborator(rSocketRequester: RSocketRequester) {
        val uuid4 = uuid4()

        println("connected new client $uuid4")

        clients[uuid4] = rSocketRequester
        streams[rSocketRequester] = MutableSharedFlow()

        rSocketRequester.rsocket()!!
            .onClose()
            .subscribe {
                clients
            }
    }

    @MessageMapping("todos")
    fun streamTodos(rSocketRequester: RSocketRequester): Flow<Operation> = streams[rSocketRequester]!!

    @MessageMapping("todos.add")
    suspend fun handleTodo(@Payload todo: Tod, rSocketRequester: RSocketRequester) {
        println("received message $todo")
        println(streams)
        streams.filter { it.key != rSocketRequester }
            .forEach { it.value.emit(todo) }
    }
}