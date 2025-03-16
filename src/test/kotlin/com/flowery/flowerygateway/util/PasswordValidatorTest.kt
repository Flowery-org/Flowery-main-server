package com.flowery.flowerygateway.utils
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test


class PasswordValidatorTest {

    @Test
    fun `should validate a valid password`() {
        val validPassword = "Valid@123"
        val result = PasswordValidator.validate(validPassword)
        assertTrue(result.isValid)
        assertTrue(result.errors.isEmpty())
    }

    @Test
    fun `should detect a short password`() {
        val shortPassword = "123"
        val result = PasswordValidator.validate(shortPassword)
        // 비밀번호가 유효하지 않아야 함
        assertFalse(result.isValid)
        // 오류 메시지에 길이 조건이 포함되어야 함
        assertTrue(result.errors.contains("Password must be at least 8 characters long"))
    }

    @Test
    fun `should detect a password `() {
        val noSpecialCharPassword = "Password123"
        val result = PasswordValidator.validate(noSpecialCharPassword)
        assertTrue(result.isValid)
        assertTrue(result.errors.isEmpty())
    }
}