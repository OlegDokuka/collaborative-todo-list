package com.example.todomvc.configuration

import com.example.todomvc.repository.TodoRepository
import com.example.todomvc.service.TodoService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AppConfiguration {

    @Bean
    fun todoService(todoRepository: TodoRepository) = TodoService(todoRepository)
}