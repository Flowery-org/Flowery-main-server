package com.flowery.flowerygateway.dto

import java.time.LocalDateTime
import java.util.*

data class Gardener (val id: UUID = UUID.randomUUID(), // 고유 식별자
                     val ident: String, // 로그인에 사용되는 사용자 식별자
                     val passwordHash: String, // 암호화된 비밀번호
                     val userName:String,
                     val nickName: String,
                     val email: String,
                     val roles: Set<String> = setOf("ROLE_USER"), // 사용자 역할. 기본값
                     val createdAt: LocalDateTime = LocalDateTime.now(), // 계정 생성 일시
                     val updatedAt: LocalDateTime = LocalDateTime.now(), // 마지막 업데이트 일시
)