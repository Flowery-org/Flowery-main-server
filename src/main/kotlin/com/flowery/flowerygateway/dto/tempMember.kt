package com.flowery.flowerygateway.dto

import jakarta.persistence.Entity
import java.util.*
import org.springframework.transaction.annotation.Transactional

@Transactional
@Entity
class tempMember(
    val ident: String,
    var password: String? = null,
    var email: String? = null,
    var name: String? = null,
    var nickname: String? = null,
) {
    val id: UUID = UUID.randomUUID()
    val token: String = "인증 서버에서"
    val createdAt: Date = Date()
    var updatedAt: Date = Date()


}