package com.flowery.flowerygateway.controller

import com.flowery.flowerygateway.dto.CodeVerifyRequestDTO
import org.springframework.web.bind.annotation.*
import com.flowery.flowerygateway.dto.FindPasswordRequestDTO
import com.flowery.flowerygateway.dto.Gardener
import com.flowery.flowerygateway.dto.PasswordRenewalRequestDTO
import com.flowery.flowerygateway.service.MailService
import com.flowery.flowerygateway.service.MemberService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import reactor.core.publisher.Mono
import java.util.*

@RestController
class SignUpController(
    @Autowired private val memberService: MemberService,
    @Autowired private val mailService: MailService) {

    // 추후 auth 서버에서 관련 service 구현 시 코드 변경 (에러 처리 추가)
    @PostMapping("/gardener/password/renewal")
    fun resetPassword(@RequestBody request: PasswordRenewalRequestDTO) : Mono<ResponseEntity<String>> {
        return memberService.updatePassword(request)
    }

    // 메일 전송을 위한 정보(RequestDTO)를 받아 Auth에 메일 전송 요청
    @PostMapping("/gardener/password")
    fun findPassword(@RequestBody request: FindPasswordRequestDTO): Mono<ResponseEntity<String>> {
        // tempMember - 추후 생성될 user 관련 entity로 대체
        val gardener: Gardener? = memberService.findByName(request.name)
        if (gardener == null || gardener.email != request.email) {
            return Mono.just(ResponseEntity.status(400).body("User not found or email mismatch"))
        }

        return mailService.sendPasswordCode(request)
            .map { result ->
                when (result) {
                    "Email sent successfully!" -> ResponseEntity.ok().body(result)
                    else -> ResponseEntity.status(404).body("Email sending failed")
                }
            }
    }

    @PostMapping("/gardener/password/code")
    fun checkVerificationCode(@RequestBody request: CodeVerifyRequestDTO) : Mono<ResponseEntity<String>> {
        return mailService.verifyCode(request)
            .map { result ->
                when (result) {
                    "Verification successful!" -> ResponseEntity.ok().body(result)
                    "Verification failed: No code found for this email." -> ResponseEntity.status(400).body(result)
                    "Verification failed: Invalid code." -> ResponseEntity.status(400).body(result)
                    else -> ResponseEntity.status(404).body("Verification failed: Server error")
                }
        }
    }

    // 추후 auth 서버에서 관련 service 구현 시 코드 변경
    @DeleteMapping("/gardener")
    fun deleteAccount(id : UUID) : ResponseEntity<String> {
        val isDeleted = memberService.deleteById(id)
        if (isDeleted) {
            return ResponseEntity.ok("Account deleted")
        } else return ResponseEntity.status(500).body("Failed to delete account")
    }
}