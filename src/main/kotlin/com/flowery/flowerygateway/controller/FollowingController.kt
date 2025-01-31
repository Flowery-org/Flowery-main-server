package com.flowery.flowerygateway.controller

import com.flowery.flowerygateway.dto.FollowingRequestDTO
import com.flowery.flowerygateway.dto.UnfollowingRequestDTO
import com.flowery.flowerygateway.service.FollowingService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono
import java.util.*

@RestController
class FollowingController(@Autowired val followingService : FollowingService) {
    // add following -> Following Request DTO (followerId, followingId)
    @PostMapping("gardener/newfollowing")
    fun newFollowing(followingRequestDTO: FollowingRequestDTO): Mono<ResponseEntity<String>> {
        return followingService.addFollowing(followingRequestDTO)
            .flatMap { response ->
                if (response.body == null || response.body!!.get("ok") == false) {
                    Mono.just(ResponseEntity.status(404).body("Following failed"))
                } else if (response.statusCode != HttpStatus.OK) {
                    Mono.just(ResponseEntity.status(response.statusCode).body("Following failed"))
                } else {
                    Mono.just(ResponseEntity.ok("Following succeeded"))
                }
            }
    }

    // following list -> (userId)
    @GetMapping("gardener/followings")
    fun getFollowing(@RequestParam id : UUID): Mono<ResponseEntity<List<UUID>>> {
        return followingService.followingList(id)
            .flatMap { list ->
                if (list.statusCode != HttpStatus.OK) {
                    Mono.just(ResponseEntity.status(list.statusCode).body(emptyList()))
                } else if (list.body == null) {
                    Mono.just(ResponseEntity.status(404).body(emptyList()))
                } else {
                    val uuidList = list.body!!.flatMap { user ->
                        user.values
                    }
                    Mono.just(ResponseEntity.ok(uuidList))
                }
            }
    }

    // delete following -> Unfollowing Request DTO (followerId, followingId)
    @DeleteMapping("gardener/unfollowing")
    fun Unfollowing(unfollowingRequestDTO: UnfollowingRequestDTO): Mono<ResponseEntity<String>> {
        return followingService.deleteFollowing(unfollowingRequestDTO)
            .flatMap { response ->
                if (response.body == null || response.body!!.get("ok") == false) {
                    Mono.just(ResponseEntity.status(404).body("Unfollowing failed"))
                } else if (response.statusCode != HttpStatus.OK) {
                    Mono.just(ResponseEntity.status(response.statusCode).body("Unfollowing failed"))
                } else {
                    Mono.just(ResponseEntity.ok("Unfollowing succeeded"))
                }
            }
    }
}