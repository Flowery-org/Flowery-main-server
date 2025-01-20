package com.flowery.flowerygateway.service

import org.springframework.http.ResponseEntity
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import org.springframework.stereotype.Service

@Service("mailService")
class MailService() {
    private val mailSender: JavaMailSender = JavaMailSenderImpl()
    fun sendPasswordCode(email: String, code: String) : Boolean {
        try {
            val message = SimpleMailMessage()
            message.setTo(email)
            message.subject = "[flowery] 비밀번호 재설정 본인 확인 메일"
            message.text = "비밀번호 재설정을 위한 본인확인 인증번호는 [${code}] 입니다."
            mailSender.send(message)
            return true
        } catch (e: Exception) {
            return false
        }
    }
}