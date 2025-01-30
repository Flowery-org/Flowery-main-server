package com.flowery.flowerygateway.service

import com.flowery.flowerygateway.dto.FindPasswordRequestDTO
import com.flowery.flowerygateway.dto.PasswordRenewalRequestDTO
import com.flowery.flowerygateway.temp.tempMember
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono
import java.util.*

@Service("memberService")
class MemberService(@Qualifier("authServiceClient") private val authService: WebClient, @Qualifier("dbServiceClient") private val dbService: WebClient) {
    // 아직 관련해서 구현된 코드가 없어서 이대로 유지 (contorller에서 에러 처리도 구현된 거 확인하고 그에 따라 작성 예정)
    fun updatePassword(request: PasswordRenewalRequestDTO) : Mono<ResponseEntity<String>> {
        val response = authService.post()
            .uri("/password") // 비밀번호 변경하는 auth 서버 function 필요
            .bodyValue(request)
            .retrieve().bodyToMono() // auth 서버에서 넘겨주는 리턴값에 따라 변경
        return response
    }

    // 추후 db 서버 연결로 변경
    fun findByName(name: String) : tempMember? = db.findById(name)
    fun deleteById(id: UUID): Boolean {
        db.deleteById(id)
        if (db.findByIdOrNull(id) == null) {
            return true
        } else return false
    }
}