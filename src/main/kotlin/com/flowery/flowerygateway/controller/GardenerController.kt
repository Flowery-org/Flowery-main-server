package com.flowery.flowerygateway.controller

import com.flowery.flowerygateway.dto.LoginRequest
import com.flowery.flowerygateway.dto.LoginResponse
import com.flowery.flowerygateway.dto.SignupRequest
import com.flowery.flowerygateway.dto.SignupResponse
import com.flowery.flowerygateway.service.AuthService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/auth")
class GardenerController (private val authService: AuthService){

    @PostMapping("/users")
    fun signUp(@RequestBody @Validated signupRequest: SignupRequest): Mono<ResponseEntity<SignupResponse>> {
        return authService.signUp(signupRequest)
            .map { response ->
                ResponseEntity.ok(response)
            }
            .onErrorResume { e ->
                when (e) {
                    is IllegalArgumentException -> Mono.just(ResponseEntity.badRequest().build())
                    is IllegalStateException -> Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).build())
                    else -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build())
                }
            }
    }

    @PostMapping("session/new")
    fun logIn(@RequestBody loginRequest: LoginRequest): Mono<ResponseEntity<LoginResponse>> {
        return authService.login(loginRequest)
            .map { response ->
                ResponseEntity.status(HttpStatus.OK).body(response)
            }
            .onErrorResume { e ->
                when (e) {
                    is IllegalArgumentException -> Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build())
                    else -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build())
                }
            }
    }

}