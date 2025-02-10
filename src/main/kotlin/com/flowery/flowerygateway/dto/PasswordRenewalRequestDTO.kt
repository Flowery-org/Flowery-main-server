package com.flowery.flowerygateway.dto

import java.util.*

data class PasswordRenewalRequestDTO (
    val id : UUID,
    val newPassword : String
)