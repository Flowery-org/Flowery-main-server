package com.flowery.flowerygateway.dto
import jakarta.validation.constraints.NotBlank

data class LoginRequest (@field:NotBlank(message = "아이디는 필수입니다")
                         val ident: String, // 사용자가 설정한 로그인 ID
                         @field:NotBlank(message = "비밀번호는 필수입니다")
                         val password: String )

