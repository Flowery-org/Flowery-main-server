package com.flowery.flowerygateway.controller

import com.flowery.flowerygateway.dto.*
import com.flowery.flowerygateway.service.AuthClientService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/main")
class AuthClientController(@Autowired val authClientService: AuthClientService) {

    @PutMapping("/gardener")
    fun signup(@RequestBody signupRequest: SignupRequest): Mono<ResponseEntity<SignupResponse>> {
        return authClientService.signUp(signupRequest)
    }

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): Mono<ResponseEntity<LoginResponse>> {
        return authClientService.login(loginRequest)

    }

    @PostMapping("/email")
    fun sendEmail(@RequestBody emailSendDto: EmailSendDto): Mono<ResponseEntity<String>> {
        return authClientService.sendEmail(emailSendDto)

    }

    @PostMapping("/verifications")
    fun verifyEmail(@RequestBody emailVerificationDto: EmailVerificationDto): Mono<ResponseEntity<String>> {
        return authClientService.verifyEmail(emailVerificationDto)
    }
}