package com.flowery.flowerygateway.utils

import com.flowery.flowerygateway.dto.ValidationResult
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component

@Component
class NickNameValidator (private val redisTemplate: RedisTemplate<String, String>) {
    //최대자수 영문 12자(영문, 숫자, 특수문자, 한글 입력 가능)
    //중복확인-> 중복일경우: 이미 사용중인 닉네임 입니다, 중복이 아닐경우: 사용가능한 닉네임 입니다.

    //최대자수: 영문 12자
    private val LENGTH_PATTERN = Regex(".{1,12}")
    //영문, 숫자, 특수문자, 한글 입력 가능
    private val NICKNAME_PATTERN = Regex("^[a-zA-Z0-9!@#\$%^&*()_+\\-\\[\\]{}|;:',.<>?/~`가-힣]*\$")

    fun validate(nickName : String) : ValidationResult {
        val errors = mutableListOf<String>()
        // 1. 길이 검증
        if (!nickName.matches(LENGTH_PATTERN)) {
            errors.add("The nickname must be between 1 and 12 characters.")
        }
        // 2. 허용되는 문자 검증
        if (!nickName.matches(NICKNAME_PATTERN)) {
            errors.add("The nickname can only contain letters, numbers, special characters, or Korean characters.")
        }
        // 3. 중복 확인
        if (isDuplicateNickname(nickName)) {
            errors.add("The nickname is already in use.")
        }

        return ValidationResult(
            isValid = errors.isEmpty(),
            errors = errors
        )
    }

    private fun isDuplicateNickname(nickName: String): Boolean =
        redisTemplate.opsForHash<String, Any>().hasKey("nickNames", nickName)

}