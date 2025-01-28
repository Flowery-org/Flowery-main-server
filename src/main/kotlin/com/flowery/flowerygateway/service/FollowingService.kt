package com.flowery.flowerygateway.service

import com.flowery.flowerygateway.dto.FollowingRequestDTO
import com.flowery.flowerygateway.dto.UnfollowingRequestDTO
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import java.util.*

@Service("followingService")
class FollowingService(@Qualifier("followingServiceClient") private val webClient : WebClient) {
    fun followingList(id : UUID) : Mono<ResponseEntity<List<Map<String, UUID>>>> {
        val response = webClient.get()
            .uri { builder -> builder.path("/rel/followings").queryParam("userId", id).build() }
            .retrieve()
            .bodyToMono(object : ParameterizedTypeReference<List<Map<String, UUID>>>() {})
            .map { body -> ResponseEntity.ok(body) }
            .onErrorResume { throwable -> Mono.just(ResponseEntity.status(500).body(emptyList()))}
        return response
    }

    fun addFollowing(followingRequestDTO: FollowingRequestDTO) : Mono<ResponseEntity<Map<String, Boolean>>> {
        val response = webClient.put()
            .uri { builder -> builder.path("/rel").build() }
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(followingRequestDTO)
            .retrieve()
            .bodyToMono(object : ParameterizedTypeReference<Map<String, Boolean>>() {})
            .map { body -> ResponseEntity.ok(body) }
            .onErrorResume { throwable -> Mono.just(ResponseEntity.status(500).body(null)) }
        return response
    }

    fun deleteFollowing(unfollowingRequestDTO: UnfollowingRequestDTO) : Mono<ResponseEntity<Map<String, Boolean>>> {
        val response = webClient.method(HttpMethod.DELETE)
            .uri { builder -> builder.path("/v1/rel").build() }
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(unfollowingRequestDTO)
            .retrieve()
            .bodyToMono(object : ParameterizedTypeReference<Map<String, Boolean>>() {})
            .map { body -> ResponseEntity.ok(body) }
            .onErrorResume { throwable -> Mono.just(ResponseEntity.status(500).body(null)) }
        return response
    }
}