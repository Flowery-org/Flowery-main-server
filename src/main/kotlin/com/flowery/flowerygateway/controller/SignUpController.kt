package com.flowery.flowerygateway.controller

import org.springframework.web.bind.annotation.*
import com.flowery.flowerygateway.dto.PasswordRequestDTO
import com.flowery.flowerygateway.dto.tempMember
import com.flowery.flowerygateway.service.MailService
import com.flowery.flowerygateway.service.tempMemberService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.*
import java.util.*

@RestController
class SignUpController(
    @Autowired private val memberService: tempMemberService,
    @Autowired private val mailService: MailService) {

    @PostMapping("/gardener/password")
    fun findPassword(@RequestBody request: PasswordRequestDTO): ResponseEntity<String> {
        // 이름 + 이메일 입력 후 코드 보내기 누르면 이 function 실행
        // 멤버 중 해당 이름 + 이메일 일치하는 멤버 있는지 확인
        val member: tempMember? = memberService.findByName(request.name)
        if (member == null || member.email != request.email) {
            return ResponseEntity.status(404).body("User not found or email mismatch")
        }

        return ResponseEntity.ok("User checked")
    }

    @PostMapping("/gardener/password/send-code")
    fun sendVerificationCode(@RequestBody request: PasswordRequestDTO) : ResponseEntity<String> {
        val code = (100000..999999).random().toString()
        mailService.sendPasswordCode(request.email!!, code)

        // 코드 입력 후 이메일과 일치하는지 여부 확인
        if (request.code != code) {
            return ResponseEntity.status(404).body("Invalid Verification Code")
        }

        return ResponseEntity.ok("Verification code sent to your email")
    }

    @PostMapping("/gardener/password/reset-password")
    fun resetPassword(@RequestBody request: PasswordRequestDTO) : ResponseEntity<String> {
        val member: tempMember? = memberService.findByName(request.name)
        if (member == null || request.newPassword == null) {
            return ResponseEntity.status(404).body("Failed to update password")
        }
        // updatePassword 결과에 따라서도 리턴값을 달리 해야 할 것 같은데...
        val isUpdated = memberService.updatePassword(member.id, member.password!!)
        if (isUpdated) {
            return ResponseEntity.ok("Password reset success")
        } else {
            return ResponseEntity.status(404).body("Failed to update password")
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