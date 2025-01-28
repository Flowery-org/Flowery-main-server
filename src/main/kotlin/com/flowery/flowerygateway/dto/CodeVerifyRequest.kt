package com.flowery.flowerygateway.dto

data class CodeVerifyRequest (
    val userEmail: String,
    val userCode: String
)