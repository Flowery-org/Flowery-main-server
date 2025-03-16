package com.flowery.flowerygateway.controller

import com.flowery.flowerygateway.dto.RateLimitResponseDTO
import com.flowery.flowerygateway.service.TrafficLimitService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
@RequestMapping("/api")
class TrafficController(private val trafficLimitService: TrafficLimitService) {

    /**
     *  서비스에서 요청 허용 여부 확인
     *
     *  @return 서비스에서 요청 허용 여부 DTO를 포함한 ResponseEntity
     *
     * **/
    @GetMapping("/limited-endpoint")
    @ResponseBody
    fun limitedEndpoint(request: HttpServletRequest): ResponseEntity<RateLimitResponseDTO> {
        val clientIp = getClientIp(request)
        val result = trafficLimitService.isRequestAllowed(clientIp)

        val response = RateLimitResponseDTO(
            success = result.allowed,
            message = if (result.allowed) "Request accepted." else "Too many requests. Please try again later.",
            clientIp = clientIp,
            retryAfter = result.retryAfter
        )

        return if (result.allowed) {
            ResponseEntity.ok(response)
        } else {
            ResponseEntity
                .status(HttpStatus.TOO_MANY_REQUESTS)
                .header("Retry-After", result.retryAfter.toString())
                .body(response)
        }
    }

    /**
     * 클라이언트 IP 가져오는 메서드
     **/
    fun getClientIp(request: HttpServletRequest): String {
        val xfHeader = request.getHeader("X-Forwarded-For")
        return if (xfHeader != null) {
            xfHeader.split(",").first() // xfHeader의 첫 번째 IP인 클라이언트 IP
        } else {
            request.remoteAddr // 클라이언트가 직접 서버에 직접 요청을 보낸 경우
        }
    }

}