package com.flowery.flowerygateway.dto

import java.time.LocalDateTime
import java.util.*

data class StatusUpdateRequestDto (
    val userId: UUID,
    val timestamp: LocalDateTime
)