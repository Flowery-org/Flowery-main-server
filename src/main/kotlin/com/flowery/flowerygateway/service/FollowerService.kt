package com.flowery.flowerygateway.service
import com.flowery.flowerygateway.dto.RemoveFollowerRequestDTO
import com.flowery.flowerygateway.util.FollowRequestValidator
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import java.util.*

@Service("followerService")
class FollowerService(@Qualifier("followingServiceClient") private val webClient : WebClient,
                      private val followRequestValidator: FollowRequestValidator) {

    //팔로워 조회
    fun getFollowerList(id: UUID): Mono<ResponseEntity<List<UUID>>> {
        return webClient.get() //HTTP GET 요청 생성
            .uri { builder -> builder.path("/rel/followers").queryParam("userId", id).build() }
            .retrieve()
            .toEntity(object : ParameterizedTypeReference<List<Map<String, UUID>>>() {})
            .flatMap { response ->
                if (response.statusCode != HttpStatus.OK) {
                    // 200 OK가 아닐 경우, 그대로 상태 코드 반환
                    Mono.just(ResponseEntity.status(response.statusCode).body(emptyList()))
                } else if (response.body == null) {
                    // body가 null이면 400 Bad Request 반환
                    Mono.just(ResponseEntity.status(400).body(emptyList()))
                } else {
                    // 정상 응답일 경우, UUID 리스트로 변환 후 반환
                    val uuidList = response.body!!.flatMap { it.values }
                    Mono.just(ResponseEntity.ok(uuidList))
                }
            }
            .onErrorResume { throwable ->
                // 예외 발생 시 500 Internal Server Error 반환
                Mono.just(ResponseEntity.status(500).body(emptyList()))
            }
    }

    fun removeFollower(
        removeFollowerRequestDTO: RemoveFollowerRequestDTO
    ): Mono<ResponseEntity<String>> {

        return followRequestValidator.validate(removeFollowerRequestDTO)
            .flatMap { validatedRequest ->
                webClient.method(HttpMethod.DELETE)
                    .uri { builder ->
                        builder.path("/v1/rel/followers")
                            .queryParam("followerId", validatedRequest.followerId)
                            .queryParam("followingId", validatedRequest.followingId)
                            .build()
                    }
                    .retrieve()
                    .toEntity(object : ParameterizedTypeReference<Map<String, Boolean>>() {})
                    .flatMap { response ->
                        when {
                            response.body?.get("ok") != true ->
                                Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Removing follower failed."))

                            response.statusCode != HttpStatus.OK ->
                                Mono.just(ResponseEntity.status(response.statusCode).body("Unfollowing failed"))

                            else ->
                                Mono.just(ResponseEntity.ok("Removing follower succeeded."))
                        }
                    }
            }
            .onErrorResume { throwable ->
                Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while removing follower: ${throwable.message}"))
            }
    }


}

