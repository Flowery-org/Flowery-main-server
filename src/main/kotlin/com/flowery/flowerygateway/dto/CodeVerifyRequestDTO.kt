package com.flowery.flowerygateway.dto

data class CodeVerifyRequestDTO (
    val userEmail: String,
    val userCode: String
)