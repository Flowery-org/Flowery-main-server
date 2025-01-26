package com.flowery.flowerygateway.utils

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class UserNameValidatorTest{
    @Test
    fun `Should validate userName`() {
        val validName = "정현수"
        val result = UserNameValidator.validate(validName)

        assertTrue(result.isValid)
        assertTrue(result.errors.isEmpty())
    }

    @Test
    fun `Should detect not korean name`() {
        val notvalidName = "username"
        val result = UserNameValidator.validate(notvalidName)

        assertFalse(result.isValid)
        assertTrue(result.errors.contains("The name must be in Korean and up to 6 characters long."))
    }
}