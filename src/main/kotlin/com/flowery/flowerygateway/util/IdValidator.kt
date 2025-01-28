package com.flowery.flowerygateway.utils
import com.flowery.flowerygateway.dto.ValidationResult
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component


@Component
class IdValidator (private val redisTemplate: RedisTemplate<String, Any>) {
    // ID 길이 제한: 4자 이상, 12자 이하
    private val LENGTH_PATTERN = Regex(".{4,12}")
    // 대문자, 소문자, 숫자, 특수문자 포함 여부
    private val CHARACTER_PATTERN = Regex(".*([A-Z]|[a-z]|\\d|[!@#\$%^&*(),.?\":{}|<>]).*")
    fun validate(ident: String): ValidationResult {
        val errors = mutableListOf<String>()
        //1. 길이 체크
        if (!ident.matches(LENGTH_PATTERN)) {
            errors.add("ident must be between 4 and 12 characters")
        }
        // 2. 최소 한 가지 조건 만족 여부 확인
        val matchesPattern = ident.matches(CHARACTER_PATTERN)
        if (!matchesPattern) {
            errors.add("The ident must contain at least one of the following: " +
                    "uppercase letters, lowercase letters, numbers, or special characters.")
        }
        //3. 중복확인
        if (isDuplicateId(ident)) {
            errors.add("The ident is already in use.")
        }
        return ValidationResult(
            isValid = errors.isEmpty(), //비어있다면(오류가 없다면) true
            errors = errors //오류 메세지 리스트
        )
    }
    //redis에 저장된 ident과 비교하여 중복 확인
    private fun isDuplicateId(ident: String): Boolean =
        redisTemplate.opsForHash<String, Any>().hasKey("gardeners", ident)
}