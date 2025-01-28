package com.flowery.flowerygateway.utils
import com.flowery.flowerygateway.dto.ValidationResult

class UserNameValidator {
    companion object {
        private val NAME_PATTERN = Regex("^[가-힣]{1,6}$")
        fun validate(name: String): ValidationResult {
            val errors = mutableListOf<String>()
            // 이름이 한글로만 이루어졌는지 확인
            if (!name.matches(NAME_PATTERN)) {
                errors.add("The name must be in Korean and up to 6 characters long.")
            }
            return ValidationResult(
                isValid = errors.isEmpty(),
                errors = errors
            )
        }
    }
}