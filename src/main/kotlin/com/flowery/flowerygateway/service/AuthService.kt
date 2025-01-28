package com.flowery.flowerygateway.service
import com.flowery.flowerygateway.dto.*
import reactor.core.publisher.Mono
interface AuthService {
    fun signUp(signUpRequest: SignupRequest): Mono<SignupResponse>
    fun login(loginRequestDto: LoginRequest): Mono<LoginResponse>
}