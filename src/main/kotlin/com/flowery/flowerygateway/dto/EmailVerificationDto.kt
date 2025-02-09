package com.flowery.flowerygateway.dto

data class EmailVerificationDto(
    val userEmail: String,
    val userCode: String
)