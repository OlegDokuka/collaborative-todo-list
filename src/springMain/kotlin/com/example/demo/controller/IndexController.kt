package com.example.demo.controller

import kotlinx.html.*
import kotlinx.html.stream.createHTML
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class IndexController {

    @RequestMapping
    @ResponseBody
    fun index(): String = createHTML().html {
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