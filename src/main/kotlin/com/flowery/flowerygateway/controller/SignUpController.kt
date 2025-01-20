package com.flowery.flowerygateway.controller

import com.flowery.flowerygateway.dto.CodeVerifyRequest
import org.springframework.web.bind.annotation.*
import com.flowery.flowerygateway.dto.PasswordRequestDTO
import com.flowery.flowerygateway.temp.tempMember
import com.flowery.flowerygateway.service.MailService
import com.flowery.flowerygateway.temp.tempMemberService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.*
import java.util.*

@RestController
class SignUpController(
    @Autowired private val memberService: tempMemberService, // tempMemberService - 추후 추가될 User 관련 Service로 대체
    @Autowired private val mailService: MailService) {

    // 메일 전송을 위한 정보(RequestDTO)를 받아 Auth에 메일 전송 요청
    @PostMapping("/gardener/password")
    fun findPassword(@RequestBody request: PasswordRequestDTO): ResponseEntity<String> {
        // tempMember - 추후 생성될 user 관련 entity로 대체
        val member: tempMember? = memberService.findByName(request.name)
        if (member == null || member.email != request.email) {
            // 404 -> 400으로 수정
            return ResponseEntity.status(400).body("User not found or email mismatch")
        }

        Boolean isSent = mailService.sendPasswordCode(request)
        if (isSent)
            return ResponseEntity.ok("Verification code sent successfully")
        } else {
            return ResponseEntity.status(404).body("Failed to send verification code")
        }
    }

    @PostMapping("/gardener/password/code")
    fun checkVerificationCode(@RequestBody request: CodeVerifyRequest) : ResponseEntity<String> {
        val isValid = mailService.verifyCode(request)
        if (isValid) {
            return ResponseEntity.ok("Verification code and input code matched")
        } else {
            return ResponseEntity.status(400).body("Wrong input code")
        }
    }

    @PostMapping("/gardener/password/renewal")
    fun resetPassword(@RequestBody request: PasswordRequestDTO) : ResponseEntity<String> {
        val isUpdated = memberService.updatePassword(member.id, member.password!!)
        if (isUpdated) {
            return ResponseEntity.ok("Password reset success")
        } else {
            return ResponseEntity.status(404).body("Failed to reset password")
        }
    }

    @DeleteMapping("/gardener")
    fun deleteAccount(id : UUID) : ResponseEntity<String> {
        val isDeleted = memberService.deleteById(id)
        if (isDeleted) {
            return ResponseEntity.ok("Account deleted")
        } else return ResponseEntity.status(500).body("Failed to delete account")
    }
}