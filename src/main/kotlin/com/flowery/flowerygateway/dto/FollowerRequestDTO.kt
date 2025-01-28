package com.flowery.flowerygateway.dto

import java.util.*

data class FollowerRequestDTO (
    val followerId: UUID,
    val followingId: UUID
)