package com.flowery.flowerygateway.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import com.flowery.flowerygateway.dto.Message
import com.flowery.flowerygateway.service.MessageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.DeleteMapping

@RestController
class MessageController(@Autowired val messageService: MessageService) {
    @PostMapping("/message")
    fun openChatting(): Mono<ResponseEntity<String>> {
        try {
            messageService.connect()
            return Mono.just(ResponseEntity.ok("Connected"))
        } catch (e: Exception) {
            return Mono.just(ResponseEntity.status(404).body("Connection Error"))
        }
    }

    @PostMapping("/message")
    fun sendMessage(@RequestBody message: Message): Mono<ResponseEntity<String>> {
        try {
            messageService.sendMessage(message)
            return Mono.just(ResponseEntity.ok("Message sent"))
        } catch (e: Exception) {
            return Mono.just(ResponseEntity.status(404).body("Message Sending Failed"))
        }
    }

    @DeleteMapping("/message")
    fun deleteMessage(@RequestBody message: Message): Mono<ResponseEntity<String>> {
        try {
            messageService.disconnect()
            return Mono.just(ResponseEntity.ok("Disconnected"))
        } catch (e: Exception) {
            return Mono.just(ResponseEntity.status(404).body("Disconnection Error"))
        }
    }
}