package com.flowery.flowerygateway.dto

data class RateLimitResponseDTO (
    val success: Boolean,  // 요청이 허용되었는지 여부
    val message: String,   // 응답 메시지
    val clientIp: String,  // 클라이언트 IP
    val retryAfter: Int? = null  // 제한 해제까지 남은 시간 (초)
)
