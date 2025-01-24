package com.flowery.flowerygateway.dto


data class PasswordRequestDTO(
    val name: String,
    val email: String,
    var newPassword: String?
)