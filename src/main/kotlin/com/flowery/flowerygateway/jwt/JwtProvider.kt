package com.flowery.flowerygateway.jwt

import io.jsonwebtoken.security.Keys
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import java.util.*
import java.util.concurrent.TimeUnit
import javax.crypto.SecretKey
import io.jsonwebtoken.Jwts


@Component
class JwtProvider (
    @Value("\${jwt.secret}") private val secretKey: String,
    @Value("\${jwt.expiration}") private val validityInMilliseconds: Long,
    private val redisTemplate: RedisTemplate<String, Any>
) {
    private val key: SecretKey by lazy { Keys.hmacShaKeyFor(secretKey.toByteArray()) }

    /**
     * 주어진 사용자명으로 새로운 JWT 토큰을 생성합니다
     *
     * @param ident 사용자 식별자
     * @param roles 사용자 역할
     * @return 생성된 JWT 토큰 문자열
     */
    fun createToken(ident: String, roles: Set<String>): String {
        val now = Date()
        val validity = Date(now.time + validityInMilliseconds)

        return Jwts.builder()
            .setSubject(ident) // 주제를 설정
            .claim("roles", roles) // 클레임 추가
            .setIssuedAt(now) // 발행 시간 설정
            .setExpiration(validity) // 만료 시간 설정
            .signWith(key) // 서명 설정
            .compact()
    }

}