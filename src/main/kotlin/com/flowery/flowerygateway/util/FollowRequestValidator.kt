package com.flowery.flowerygateway.util

import com.flowery.flowerygateway.dto.RemoveFollowerRequestDTO
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
@Component
class FollowRequestValidator {

    fun validate(request: RemoveFollowerRequestDTO): Mono<RemoveFollowerRequestDTO> {

        if (request.targetId == request.requesterId) {
            return Mono.error(IllegalArgumentException("Target ID and Requester ID cannot be the same."))
        }

        return Mono.just(request)
    }
}