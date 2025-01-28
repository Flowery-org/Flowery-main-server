package com.flowery.flowerygateway.utils
import com.flowery.flowerygateway.dto.Gardener
import com.flowery.flowerygateway.dto.ValidationResult
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
@Component
class EmailValidator(private val redisTemplate: RedisTemplate<String, Any>) {
    // 이메일 형식 검사 정규식
    private val EMAIL_PATTERN = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-z]{2,}$")

    fun validate(email: String) : ValidationResult {
        val errors = mutableListOf<String>()
        //1. 최대 길이 확인
        if (email.length > 100) {
            errors.add("Email must not exceed 100 characters.")
        }
        //2. 이메일형식 검사
        if (!email.matches(EMAIL_PATTERN)) {
            errors.add("Invalid email format.")
        }
        //3. 중복확인
        if (isDuplicateEmail(email)) {
            errors.add("This email is already in use.")
        }
        return ValidationResult(
            isValid = errors.isEmpty(),
            errors = errors
        )
    }
    private fun isDuplicateEmail(email: String): Boolean =
        redisTemplate.opsForHash<String, Any>().hasKey("emails", email)
}