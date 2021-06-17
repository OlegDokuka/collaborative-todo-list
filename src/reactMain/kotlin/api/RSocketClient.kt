package api

import com.example.demo.model.Todo
import com.example.demo.model.TodoEvent
import io.ktor.client.*
import io.ktor.client.features.websocket.*
import io.ktor.utils.io.core.*
import io.rsocket.kotlin.ExperimentalMetadataApi
import io.rsocket.kotlin.RSocket
import io.rsocket.kotlin.core.RSocketConnector
import io.rsocket.kotlin.core.WellKnownMimeType
import io.rsocket.kotlin.metadata.CompositeMetadata
import io.rsocket.kotlin.metadata.RoutingMetadata
import io.rsocket.kotlin.metadata.metadata
import io.rsocket.kotlin.payload.PayloadMimeType
import io.rsocket.kotlin.payload.buildPayload
import io.rsocket.kotlin.payload.data
import io.rsocket.kotlin.transport.ktor.client.RSocketSupport
import io.rsocket.kotlin.transport.ktor.client.rSocket
import kotlinx.browser.window
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


@OptIn(ExperimentalMetadataApi::class)
class RSocketClient(val rsocket: RSocket) : Client {

    override fun handleTodos(handler: (TodoEvent) -> Unit) {
        rsocket
            .requestStream(buildPayload {
                data(ByteReadPacket.Empty)
                metadata(CompositeMetadata(RoutingMetadata("todos")))
            })
            .map {
                val string = it.data.readText()
                Json.decodeFromString<TodoEvent>(string)
            }
            .onEach {
                handler(it)
            }
            .launchIn(MainScope())
    }

    override fun exchange(todo: List<Todo>) {
        MainScope().launch {
            todo.forEach {
                rsocket
                    .fireAndForget(buildPayload {
                        data(Json.encodeToString(todo))
                        metadata(CompositeMetadata(RoutingMetadata("todos.upsert")))
                    })
            }
        }
    }

    override fun addTodo(todo: Todo) {
        MainScope().launch {
            rsocket
                .fireAndForget(buildPayload {
                    data(Json.encodeToString(todo))
                    metadata(CompositeMetadata(RoutingMetadata("todos.add")))
                })
        }
    }

    override fun updateTodo(todo: Todo) {
        MainScope().launch {
            rsocket
                .fireAndForget(buildPayload {
                    data(Json.encodeToString(todo))
                    metadata(CompositeMetadata(RoutingMetadata("todos.update")))
                })
        }
    }

    override fun removeTodo(todo: Todo) {
        MainScope().launch {
            rsocket
                .fireAndForget(buildPayload {
                    data(Json.encodeToString(todo))
                    metadata(CompositeMetadata(RoutingMetadata("todos.remove")))
                })
        }
    }

    companion object {
        suspend fun create(): Client {
            val client: HttpClient = HttpClient {
                install(WebSockets)
                install(RSocketSupport) {
                    connector = RSocketConnector {
                        connectionConfig {
                            payloadMimeType = PayloadMimeType(
                                data = WellKnownMimeType.ApplicationJson,
                                metadata = WellKnownMimeType.MessageRSocketCompositeMetadata
                            )
                        }
                    }
                }
            }

            val rSocket = client.rSocket(
                host = window.location.hostname,
                port = window.location.port.toInt(),
                path = "/rsocket"
            )

            return RSocketClient(rSocket)
        }
    }
}