package com.flowery.flowerygateway.dto

import java.util.*

class UnfollowingRequestDTO(
    val followerId: UUID,
    val followingId: UUID
)