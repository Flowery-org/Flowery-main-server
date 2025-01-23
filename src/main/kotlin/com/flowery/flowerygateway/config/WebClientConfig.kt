package com.flowery.flowerygateway.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfig {
    @Bean
    fun webClientBuilder(): WebClient.Builder {
        return WebClient.builder()
    }

    @Bean
    fun authServiceClient(): WebClient {
        return WebClient.builder()
            .baseUrl("http://localhost:6379/api/auth/")
            .build()
    }

    @Bean
    fun dbServiceClient() : WebClient {
        return WebClient.builder()
            .baseUrl("http://localhost:6379/api/")
            .build()
    }
}