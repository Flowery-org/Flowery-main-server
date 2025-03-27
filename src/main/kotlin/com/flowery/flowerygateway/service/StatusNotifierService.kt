package com.flowery.flowerygateway.service

import com.flowery.flowerygateway.dto.StatusUpdateRequestDto
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.time.LocalDateTime
import java.util.*

@Service
class StatusNotifierService (private val restTemplate: RestTemplate,
                             @Value("\${status-server.url}") private val statusServerUrl: String){
    private val log = LoggerFactory.getLogger(StatusNotifierService::class.java)

    fun notifyActivity(userId: UUID): Boolean {
        val request = StatusUpdateRequestDto(
            userId = userId,
            timestamp = LocalDateTime.now()
        )
        val url = "$statusServerUrl/status/update"
        return try {
            val result = restTemplate.postForObject(url, request, Boolean::class.java)
            if (result == true) {
                log.info(" status server 전송 성공 - userId : $userId")
                true
            } else {
                log.warn(" status server 응답이 false - userId : $userId")
                false
            }
        } catch (e: Exception) {
            log.error(" status server 전송 실패 - userId: $userId, error: ${e.message}", e)
            false
        }
    }
}