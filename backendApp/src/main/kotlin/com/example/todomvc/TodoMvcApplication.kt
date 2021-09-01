package com.example.todomvc

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication
class TodoMvcApplication

fun main(args: Array<String>) {
    runApplication<TodoMvcApplication>(*args)
}
