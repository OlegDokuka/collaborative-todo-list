package com.example.todomvc.controller

import kotlinx.html.*
import kotlinx.html.stream.createHTML
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class IndexController {

    @GetMapping
    @ResponseBody
    fun index() = createHTML().html {
        head {
            title("ToDo List")
        }
        body {
            div {
                id = "root"
            }
            script(src = "/main.js") {}
        }
    }
}