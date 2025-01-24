package com.flowery.flowerygateway.service

import com.flowery.flowerygateway.dto.CodeVerifyRequest
import com.flowery.flowerygateway.dto.PasswordRequestDTO
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Service("mailService")
class MailService(@Qualifier("authServiceClient") private val webClient: WebClient) {
    fun verifyCode(request: CodeVerifyRequest) : Mono<String> {
        val response = webClient.post()
            .uri("/verification")
            .bodyValue(request)
            .retrieve().bodyToMono(String::class.java)
        return response
    }

    fun sendPasswordCode(request: PasswordRequestDTO) : Mono<String> {
        val response = webClient.post()
            .uri("/emails")
            .bodyValue(request)
            .retrieve().bodyToMono(String::class.java)
        return response
    }
}