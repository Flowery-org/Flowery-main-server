package com.flowery.flowerygateway.service

import org.springframework.stereotype.Service
import redis.clients.jedis.JedisPooled

@Service
class TrafficLimitService {

    private val jedisClient = JedisPooled("localhost", 6379)
    private val cacheKeyPrefix = "rate-limit:" // redis key prefix
    private val maxRequests = 10 // 요청 횟수 최대 10
    private val timeWindowSeconds = 60 // 1분 단위로 제한

    data class RateLimitResult(
        val allowed: Boolean,
        val retryAfter: Int?
    )

    // 요청을 처리할 수 있는지 확인
    fun isRequestAllowed(ip: String): RateLimitResult {
        val key = "$cacheKeyPrefix$ip"
        val currentCount = jedisClient.get(key)?.toIntOrNull() ?: 0

        return if (currentCount < maxRequests) {
            jedisClient.setex(key, timeWindowSeconds.toLong(), (currentCount + 1).toString())  // 요청 수 증가
            RateLimitResult(allowed = true, retryAfter = null)
        } else {
            val ttl = jedisClient.ttl(key).toInt()  // 현재 TTL 조회
            RateLimitResult(allowed = false, retryAfter = ttl)
        }
    }

}