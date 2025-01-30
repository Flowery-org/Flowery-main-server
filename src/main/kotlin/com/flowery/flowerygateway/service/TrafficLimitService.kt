package com.flowery.flowerygateway.service

import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap
import io.github.bucket4j.Bucket
import io.github.bucket4j.Bandwidth
import java.time.Duration

@Service
class TrafficLimitService {

    // IP별로 Bucket을 관리
    private val buckets: ConcurrentHashMap<String, Bucket> = ConcurrentHashMap()

    // IP에 해당하는 Bucket 생성 또는 조회
    fun getBucket(ip: String): Bucket{
        val bucket = buckets.computeIfAbsent(ip) {
            Bucket.builder()
                .addLimit(Bandwidth.simple(10, Duration.ofMinutes(1)) )// 분당 10 요청
                .build()
        }
        return bucket
    }

    // 요청을 처리할 수 있는지 확인
    fun isRequestAllowed(ip: String): Boolean {
        val bucket = getBucket(ip)
        val result =  bucket.tryConsume(1)

        return result
    }

}