package com.flowery.flowerygateway.dto


data class PasswordRequestDTO(
    val name: String,
    val email: String,
    val ident: String? = null,
    var code: String? = null,
    var newPassword: String? = null
)