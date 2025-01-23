package com.flowery.flowerygateway.service

import com.flowery.flowerygateway.dto.PasswordRequestDTO
import com.flowery.flowerygateway.temp.tempMember
import com.flowery.flowerygateway.temp.tempMemberRepository
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.jpa.domain.AbstractPersistable_.id
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.body
import org.springframework.web.reactive.function.client.toEntity
import java.util.*

// main server에서 db에 직접 접근 가능한건지, 아니면 auth 서버에 요청해야 하는건지?
@Service("memberService")
class MemberService(private val db: tempMemberRepository, @Qualifier("authServiceClient") private val webClient: WebClient) {
    fun findByName(name: String) : tempMember? = db.findByName(name)
    fun updatePassword(request: PasswordRequestDTO) : ResponseEntity<String>? {
        val response = webClient.post()
            .uri("/password") // 비밀번호 변경하는 auth 서버 function 필요
            .bodyValue(request)
            .retrieve().toEntity(String::class.java)
            .block()
        return response
    }
    fun deleteById(id: UUID): Boolean {
        db.deleteById(id)
        if (db.findByIdOrNull(id) == null) {
            return true
        } else return false
    }
}