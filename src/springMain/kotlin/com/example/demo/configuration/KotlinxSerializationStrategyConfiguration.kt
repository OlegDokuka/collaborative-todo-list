package com.example.demo.configuration

import kotlinx.serialization.json.Json
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.rsocket.messaging.RSocketStrategiesCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.http.codec.json.KotlinSerializationJsonDecoder
import org.springframework.http.codec.json.KotlinSerializationJsonEncoder
import org.springframework.messaging.rsocket.RSocketStrategies


@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(
    Json::class,
)
class KotlinxSerializationStrategyConfiguration {
    @Bean
    @Order(0)
    fun kotlinxSerializationRSocketStrategyCustomizer(): RSocketStrategiesCustomizer {
        return RSocketStrategiesCustomizer { strategy: RSocketStrategies.Builder ->
            strategy.decoder(KotlinSerializationJsonDecoder())
            strategy.encoder(KotlinSerializationJsonEncoder())
        }
    }
}