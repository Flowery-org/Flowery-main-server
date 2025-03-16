package com.flowery.flowerygateway.dto

import com.flowery.flowerygateway.constants.MessageType

data class Message (
    val type: MessageType,
    val senderId: String,
    val receiverId: String,
    val payload: String,
    val timestamp: Long = System.currentTimeMillis(),
)