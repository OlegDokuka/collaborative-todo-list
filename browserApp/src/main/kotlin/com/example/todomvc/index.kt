package com.example.todomvc

import com.example.todomvc.ui.app.AppOptions
import com.example.todomvc.ui.app.app
import com.example.todomvc.client.RSocketClient
import com.example.todomvc.client.create
import com.example.todomvc.service.TodoService
import kotlinext.js.require
import kotlinext.js.requireAll
import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.events.EventListener
import react.dom.render
import com.example.todomvc.repository.LocalStorageTodoRepository
import kotlin.collections.set


suspend fun main(args: Array<String>) {
    initStyles()
    AppOptions.language = "en_US"

    val client = RSocketClient.create()
    val service = TodoService(LocalStorageTodoRepository())

    fun render(route: String = "list", parameters: Map<String, String>) {
        render(document.getElementById("root")) {
            app(route, client, service)
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

fun initStyles() {
    requireAll(require.context("", true, js("/\\.css$/")))
    requireAll(require.context("../../../node_modules/todomvc-app-css", true, js("/\\.css$/")))
    requireAll(require.context("../../../node_modules/todomvc-common", true, js("/\\.css$/")))
    requireAll(require.context("../../../node_modules/todomvc-common", true, js("/\\.js$/")))
}
