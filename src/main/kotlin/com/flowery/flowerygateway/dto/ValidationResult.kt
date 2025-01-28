package com.flowery.flowerygateway.dto

class ValidationResult (
    val isValid: Boolean,
    val errors: MutableList<String>
)