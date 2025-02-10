package com.flowery.flowerygateway.service
import com.flowery.flowerygateway.dto.*
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Service
class AuthClientService(@Qualifier ("authServiceClient") val webClient : WebClient) {

    fun signUp(signupRequest: SignupRequest): Mono<ResponseEntity<SignupResponse>> {
        return webClient.post()
            .uri("users")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(signupRequest)
            .exchangeToMono { response ->
                if (response.statusCode().is2xxSuccessful) {
                    // 2xx 상태인 경우, 응답 본문을 SignupResponse로 변환하여 검사
                    response.bodyToMono(SignupResponse::class.java)
                        .map { body ->
                            if (body == null) {
                                // 바디가 null이면 404 Not Found로 처리
                                ResponseEntity.status(HttpStatus.NOT_FOUND).build<SignupResponse>()
                            } else {
                                // 정상 응답인 경우 그대로 200 OK 반환
                                ResponseEntity.ok(body)
                            }
                        }
                } else {
                    // 2xx가 아닌 상태인 경우, 원래 상태 코드를 그대로 반환
                    Mono.just(ResponseEntity.status(response.statusCode()).build<SignupResponse>())
                }
            }
            .onErrorResume { throwable ->
                when {
                    throwable.message?.contains("이미 존재하는 아이디입니다") == true -> {
                        Mono.just(ResponseEntity.status(409).build<SignupResponse>())
                    }

                    throwable.message?.contains("비밀번호 유효성 검사 실패") == true -> {
                        Mono.just(ResponseEntity.status(400).build<SignupResponse>())
                    }

                    else -> {
                        // 기타 예외 처리
                        Mono.just(ResponseEntity.status(500).build<SignupResponse>())
                    }
                }
            }
    }

    fun login(loginRequest: LoginRequest): Mono<ResponseEntity<LoginResponse>> {
        return webClient.post()
            .uri("")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(loginRequest)
            .exchangeToMono { response ->
                if (response.statusCode().is2xxSuccessful) {
                    response.bodyToMono(LoginResponse::class.java)
                        .map { body ->
                            if (body == null) {
                                //바디가 null이면 404 Not Found로 처리
                                ResponseEntity.status(404).build<LoginResponse>()
                            } else {
                                // 정상 응답인 경우 그대로 200 OK 반환
                                ResponseEntity.ok(body)
                            }
                        }
                } else {
                    // 2xx가 아닌 상태인 경우, 원래 상태 코드를 그대로 반환하고 body는 null
                    Mono.just(ResponseEntity.status(response.statusCode()).build<LoginResponse>())
                }
            }
            .onErrorResume { throwable ->
                // 예외 발생 시 500 Internal Server Error 반환
                Mono.just(ResponseEntity.status(500).build<LoginResponse>())
            }
    }

    fun sendEmail(emailSendDto: EmailSendDto): Mono<ResponseEntity<String>> {
        return webClient.post()
            .uri("emails")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(emailSendDto)
            .exchangeToMono { response ->
                if (response.statusCode().is2xxSuccessful) {
                    // 정상 응답인 경우, 응답 본문을 String으로 변환
                    response.bodyToMono(object : ParameterizedTypeReference<String>() {})
                        .map { body ->
                            when {
                                // 200 OK이지만 응답 본문에 "Email sent failed!"가 포함된 경우
                                body.contains("Email sent failed!") ->
                                    ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email sent failed!")
                                // 응답 본문이 null인 경우
                                body.isEmpty() ->
                                    ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found")
                                // 정상 응답인 경우 그대로 200 OK 반환
                                else ->
                                    ResponseEntity.ok(body)
                            }
                        }
                } else {
                    // 2xx가 아닌 상태인 경우, 원래 상태 코드를 그대로 반환
                    Mono.just(ResponseEntity.status(response.statusCode()).body("Email sent failed!"))
                }
            }
            .onErrorResume { throwable ->
                // 네트워크 오류, JSON 변환 오류, 기타 예외 발생 시 500 Internal Server Error 반환
                Mono.just(ResponseEntity.status(500).body("Internal Server Error"))
            }
    }


    fun verifyEmail(emailVerificationDto: EmailVerificationDto): Mono<ResponseEntity<String>> {
        return webClient.post()
            .uri("verifications")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(emailVerificationDto)
            .exchangeToMono { response ->
                if (response.statusCode().is2xxSuccessful) {
                    // 정상 응답인 경우, 응답 본문을 String으로 변환
                    response.bodyToMono(object : ParameterizedTypeReference<String>() {})
                        .map { body ->
                            when {
                                // 200 OK이지만 응답 본문에 "Verification failed: Invalid code."가 포함된 경우
                                body.contains("Verification failed: Invalid code.") ->
                                    ResponseEntity.status(HttpStatus.NOT_FOUND)
                                        .body("Verification failed: Invalid code.")
                                // 응답 본문이 빈 문자열
                                body.isEmpty() ->
                                    ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found")
                                // 정상 응답인 경우 그대로 200 OK 반환
                                else ->
                                    ResponseEntity.ok(body)
                            }
                        }
                } else {
                    // 2xx가 아닌 상태인 경우, 원래 상태 코드를 그대로 반환
                    Mono.just(
                        ResponseEntity.status(response.statusCode()).body("Verification failed: Invalid code.")
                    )
                }
            }
            .onErrorResume { throwable ->
                // 네트워크 오류, JSON 변환 오류, 기타 예외 발생 시 500 Internal Server Error 반환
                Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error"))
            }
    }
}