package com.example.demo.configuration

import com.example.demo.repository.TodoRepository
import com.example.demo.service.TodoService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AppConfiguration {

    @Bean
    fun todoService(todoRepository: TodoRepository) = TodoService(todoRepository)
}