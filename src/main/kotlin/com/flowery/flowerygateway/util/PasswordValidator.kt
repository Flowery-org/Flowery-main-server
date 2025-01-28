package com.flowery.flowerygateway.utils
import com.flowery.flowerygateway.dto.ValidationResult

//영어 소문자, 대문자, 숫자, 특수기호 중 2종류 이상으로 구성된 8자리 이상 문자열
class PasswordValidator {
    companion object {

        private val LENGTH_PATTERN = Regex(".{8,}")  // 8자 이상
        private val UPPERCASE_PATTERN = Regex(".*[A-Z].*")  // 대문자 포함
        private val LOWERCASE_PATTERN = Regex(".*[a-z].*")  // 소문자 포함
        private val NUMBER_PATTERN = Regex(".*\\d.*")  // 숫자 포함
        private val SPECIAL_CHAR_PATTERN = Regex(".*[!@#\$%^&*(),.?\":{}|<>].*")  // 특수문자 포함
        fun validate(password: String): ValidationResult {
            val errors = mutableListOf<String>()
            //길이 체크 : 8글자 이상인가?
            if (!password.matches(LENGTH_PATTERN)) {
                println("Password length is less than 8: $password") // 디버깅용 출력
                errors.add("Password must be at least 8 characters long")
            }
            // 각 패턴 충족 여부
            val matchCount = listOf(
                password.matches(UPPERCASE_PATTERN),  // 대문자 포함 여부
                password.matches(LOWERCASE_PATTERN),  // 소문자 포함 여부
                password.matches(NUMBER_PATTERN),     // 숫자 포함 여부
                password.matches(SPECIAL_CHAR_PATTERN) // 특수문자 포함 여부
            ).count { it }
            // 최소 2종류 이상 충족해야 함
            if (matchCount < 2) {
                errors.add("The password must contain at least two of the following: " +
                        "lowercase letters, uppercase letters, numbers, or special characters.")
            }
            return ValidationResult(
                isValid = errors.isEmpty(), //비어있다면(오류가 없다면) true
                errors = errors //오류 메세지 리스트
            )
        }
    }
}