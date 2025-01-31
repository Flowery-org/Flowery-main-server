package com.flowery.flowerygateway.controller

import com.flowery.flowerygateway.dto.RemoveFollowerRequestDTO
import com.flowery.flowerygateway.service.FollowerService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import java.util.*

@RestController
class FollowerController(private val followerService: FollowerService) {

    @PostMapping("gardener/removeFollower")
    fun removeFollowers(@RequestBody removeFollowerRequestDTO : RemoveFollowerRequestDTO) : Mono<ResponseEntity<String>> {
        return followerService.removeFollower(removeFollowerRequestDTO)
    }


    @DeleteMapping("gardener/followers")
    fun getFollowers(@RequestParam id: UUID): Mono<ResponseEntity<List<UUID>>> {
        return followerService.getFollowerList(id) // UUID 리스트를 바로 반환
    }
}

