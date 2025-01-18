package com.flowery.flowerygateway.controller

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

    @PostMapping("/gardener/password")
    fun findPassword(@RequestBody request: PasswordRequestDTO): ResponseEntity<String> {
        // 이름 + 이메일 입력 후 코드 보내기 누르면 이 function 실행
        // 멤버 중 해당 이름 + 이메일 일치하는 멤버 있는지 확인

        // tempMember - 추후 생성될 user 관련 entity로 대체
        val member: tempMember? = memberService.findByName(request.name)
        if (member == null || member.email != request.email) {
            return ResponseEntity.status(404).body("User not found or email mismatch")
        }

        return ResponseEntity.ok("User verified")
    }

    @PostMapping("/gardener/password/email")
    fun sendVerificationCode(@RequestBody request: PasswordRequestDTO) : ResponseEntity<String> {
        val code = (100000..999999).random().toString()
        val isSent = mailService.sendPasswordCode(request.email!!, code)
        if (isSent) {
            return ResponseEntity.ok("Verification code sent successfully")
        } else {
            return ResponseEntity.status(404).body("Verification code sending failed")
        }
    }

    @PostMapping("/gardener/password/code")
    fun validateVerificationCode(@RequestBody request: PasswordRequestDTO) : ResponseEntity<String> {
        val isValid = mailService.validateCode(request.email!!, request.code!!)
        if (isValid) {
            return ResponseEntity.ok("Verification code and input code matched")
        } else {
            return ResponseEntity.status(400).body("Wrong input code")
        }
    }

    @PostMapping("/gardener/password/renewal")
    fun resetPassword(@RequestBody request: PasswordRequestDTO) : ResponseEntity<String> {
        val member: tempMember? = memberService.findByName(request.name)
        // tempMember: 추후 생성될 member entity
        if (member == null || request.newPassword == null) {
            return ResponseEntity.status(404).body("Failed to update password")
        }

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