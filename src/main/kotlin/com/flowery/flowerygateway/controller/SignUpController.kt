package com.flowery.flowerygateway.controller

import com.flowery.flowerygateway.dto.CodeVerifyRequest
import org.springframework.web.bind.annotation.*
import com.flowery.flowerygateway.dto.PasswordRequestDTO
import com.flowery.flowerygateway.temp.tempMember
import com.flowery.flowerygateway.service.MailService
import com.flowery.flowerygateway.service.MemberService
import com.flowery.flowerygateway.temp.tempMemberService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.*
import java.util.*

@RestController
class SignUpController(
    @Autowired private val memberService: MemberService,
    @Autowired private val mailService: MailService) {

    // 메일 전송을 위한 정보(RequestDTO)를 받아 Auth에 메일 전송 요청
    @PostMapping("/gardener/password")
    fun findPassword(@RequestBody request: PasswordRequestDTO): ResponseEntity<String>? {
        // tempMember - 추후 생성될 user 관련 entity로 대체
        val member: tempMember? = memberService.findByName(request.name)
        if (member == null || member.email != request.email) {
            return ResponseEntity.status(400).body("User not found or email mismatch")
        }

        return mailService.sendPasswordCode(request)
    }

    @PostMapping("/gardener/password/code")
    fun checkVerificationCode(@RequestBody request: CodeVerifyRequest) : ResponseEntity<String>? {
        return mailService.verifyCode(request)
    }

    @PostMapping("/gardener/password/renewal")
    fun resetPassword(@RequestBody request: PasswordRequestDTO) : ResponseEntity<String>? {
        return memberService.updatePassword(request)
    }

    @DeleteMapping("/gardener")
    fun deleteAccount(id : UUID) : ResponseEntity<String> {
        val isDeleted = memberService.deleteById(id)
        if (isDeleted) {
            return ResponseEntity.ok("Account deleted")
        } else return ResponseEntity.status(500).body("Failed to delete account")
    }
}