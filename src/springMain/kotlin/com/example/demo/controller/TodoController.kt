package com.example.demo.controller

import com.benasher44.uuid.Uuid
import com.benasher44.uuid.uuid4
import com.example.demo.model.EventType
import com.example.demo.model.Todo
import com.example.demo.model.TodoEvent
import com.example.demo.service.TodoService
import kotlinx.coroutines.flow.*
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.rsocket.RSocketRequester
import org.springframework.messaging.rsocket.annotation.ConnectMapping
import org.springframework.stereotype.Controller
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

@Controller
class TodoController(val todoService: TodoService) {

    val clients: ConcurrentMap<Uuid, RSocketRequester> = ConcurrentHashMap()
    val streams: ConcurrentMap<RSocketRequester, MutableSharedFlow<TodoEvent>> = ConcurrentHashMap()

    @ConnectMapping("")
    fun handleCollaborator(rSocketRequester: RSocketRequester) {
        val uuid4 = uuid4()

        println("connected new client $uuid4")

        clients[uuid4] = rSocketRequester
        streams[rSocketRequester] = MutableSharedFlow()

        rSocketRequester.rsocket()!!
            .onClose()
            .subscribe {
                clients.remove(uuid4)
                streams.remove(rSocketRequester)
            }
    }

    @MessageMapping("todos")
    fun streamTodos(rSocketRequester: RSocketRequester): Flow<TodoEvent> =
        todoService.listTodos().asFlow()
            .map { TodoEvent(EventType.UPSERT, it) }
            .onCompletion { emitAll(streams[rSocketRequester]!!) }


    @MessageMapping("todos.{action}")
    suspend fun handleTodoAction(
        @Payload todo: Todo,
        @DestinationVariable("action") action: String,
        rSocketRequester: RSocketRequester
    ) {
        val evenType = when (action) {
            "add" -> EventType.ADD
            "update" -> EventType.UPDATE
            "remove" -> EventType.REMOVE
            "upsert" -> EventType.UPSERT
            else -> throw Error("Unsupported action type $action")
        }
        val todoEvent = TodoEvent(evenType, todo)

        todoService.handleEvent(todoEvent)
        streams
            .filter { it.key != rSocketRequester }
            .forEach {
                it.value.emit(todoEvent)
            }
    }
}