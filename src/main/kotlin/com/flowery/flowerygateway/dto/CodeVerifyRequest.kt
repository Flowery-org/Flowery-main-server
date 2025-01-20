package com.flowery.flowerygateway.dto

data class CodeVerifyRequest (
    val name: String,
    val email: String,
    val inputCode: String
)