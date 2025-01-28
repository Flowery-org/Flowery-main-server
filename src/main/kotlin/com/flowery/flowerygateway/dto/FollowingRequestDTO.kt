package com.flowery.flowerygateway.dto

import java.util.*

data class FollowingRequestDTO(
    val followerId: UUID,
    val followingId: UUID
)