package index

import app.*
import io.ktor.client.*
import io.ktor.client.features.websocket.*
import io.rsocket.kotlin.core.RSocketConnector
import io.rsocket.kotlin.core.WellKnownMimeType
import io.rsocket.kotlin.metadata.buildCompositeMetadata
import io.rsocket.kotlin.payload.PayloadMimeType
import io.rsocket.kotlin.payload.buildPayload
import io.rsocket.kotlin.payload.data
import io.rsocket.kotlin.transport.ktor.client.RSocketSupport
import io.rsocket.kotlin.transport.ktor.client.rSocket
import kotlinext.js.*
import org.w3c.dom.events.EventListener
import react.dom.*
import kotlinx.browser.*
import react.redux.provider
import reducers.State
import reducers.combinedReducers
import redux.*

//@JsModule("redux-thunk.default")
//@JsNonModule
//external val thunk: Middleware<State, RAction, WrapperAction, RAction, dynamic>

//val store: Store<State, RAction, dynamic> = createStore<State, RAction, dynamic>(
//    combinedReducers(),
//    State(),
//    compose(
//        applyMiddleware(thunk, ),
//        rEnhancer(),
//        js("if(window.__REDUX_DEVTOOLS_EXTENSION__ )window.__REDUX_DEVTOOLS_EXTENSION__ ();else(function(f){return f;});")
//    )
//)

suspend fun main(args: Array<String>) {
    val client: HttpClient = HttpClient {
        install(WebSockets)
        install(RSocketSupport) {
            connector = RSocketConnector {
                connectionConfig {
                    payloadMimeType = PayloadMimeType(data = WellKnownMimeType.ApplicationJson, metadata = WellKnownMimeType.MessageRSocketCompositeMetadata)
                }
            }
        }
    }
    val rSocket = client.rSocket(port = 8080, path = "/rsocket")


    requireAll(require.context("", true, js("/\\.css$/")))
    requireAll(require.context("../../../node_modules/todomvc-app-css", true, js("/\\.css$/")))
    requireAll(require.context("../../../node_modules/todomvc-common", true, js("/\\.css$/")))
    requireAll(require.context("../../../node_modules/todomvc-common", true, js("/\\.js$/")))

    AppOptions.language = "en_US"

    fun render(route: String = "list", parameters: Map<String, String>) {
        render(document.getElementById("root")) {
//            provider(store) {
                app(route, rSocket)
//            }
        }
    }

    fun renderByUrl() {
        val href = window.location.href

        if (!href.contains("?")) {
            render(parameters = emptyMap())
            return
        }

        val parametersString = href.split("?")[1]
        val parameters = parametersString.split("&")

        val map = mutableMapOf<String, String>()
        parameters.forEach { parameterString ->
            if (parameterString.contains("=")) {
                val params = parameterString.split("=")
                map[params[0]] = params[1]
            }
        }

        val route = map["route"]
        if (route != null) {
            render(route = route, parameters = map)
        } else {
            render(parameters = map)
        }
    }

    window.addEventListener("hashchange", EventListener {
        renderByUrl()
    })

    renderByUrl()

}
