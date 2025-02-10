package com.flowery.flowerygateway.dto

import java.util.*

data class RemoveFollowerRequestDTO (
    val followerId: UUID, //삭제를 당하는 유저 아이디
    val followingId: UUID //삭제를 요청하는 유저 아이디
)

