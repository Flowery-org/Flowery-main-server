package com.flowery.flowerygateway.dto
import jakarta.validation.constraints.NotNull
import java.util.UUID
import java.util.*

data class FollowerRequestDTO (
    @field:NotNull(message = "targetId cannot be null")
    val followerId: UUID, //언팔로우 당함
    @field:NotNull(message = "requesterId cannot be null")
    val followingId: UUID //팔로워를 delete하는 주체
)
