package com.example.todomvc.client

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.websocket.*
import io.rsocket.kotlin.core.RSocketConnector
import io.rsocket.kotlin.core.WellKnownMimeType
import io.rsocket.kotlin.payload.PayloadMimeType
import io.rsocket.kotlin.transport.ktor.client.RSocketSupport
import io.rsocket.kotlin.transport.ktor.client.rSocket

actual suspend fun RSocketClient.Companion.create(): Client {
    val client: HttpClient = HttpClient(OkHttp) {
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
        host = "192.168.1.44",
        port = 8080,
        path = "/rsocket"
    )

    return RSocketClient(rSocket)
}