package com.flowery.flowerygateway.controller

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
     *  허용, 버킷에서 토큰(요청 가능 횟수)을 차감하고 요청을 처리
     *  거부, 버킷이 비어 있으면 요청을 거부하고 429 Too Many Requests 응답을 반환
     * **/
    @GetMapping("/limited-endpoint")
    @ResponseBody
    fun limitedEndpoint(request : HttpServletRequest): ResponseEntity<String> {
        val clientIp = getClientIp(request)

        return if (trafficLimitService.isRequestAllowed(clientIp)){
            ResponseEntity.ok("Request accepted. Your IP: $clientIp")
        }
        else {
            ResponseEntity
                .status(HttpStatus.TOO_MANY_REQUESTS)
                .header("Retry-After", "60") // 제한 해제까지 60초
                .body("Too many requests. Please try again later.")
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