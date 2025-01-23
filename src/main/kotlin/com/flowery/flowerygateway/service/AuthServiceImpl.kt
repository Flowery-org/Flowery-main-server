package com.flowery.flowerygateway.service
//import com.flowery.flowerygateway.Repository.AuthRepository
import com.flowery.flowerygateway.dto.*
import com.flowery.flowerygateway.jwt.JwtProvider
import com.flowery.flowerygateway.utils.PasswordValidator
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class AuthServiceImpl (
    private val jwtProvider: JwtProvider,
    private val passwordEncoder: PasswordEncoder,
    private val redisTemplate: RedisTemplate<String, Any>,
    //private val authRepository: AuthRepository

) : AuthService {
    override fun signUp(signUpRequest: SignupRequest): Mono<SignupResponse> {
        //password 유효성 검사
        validatePassword(signUpRequest.password)

        //password Hashing
        val passwordHash = passwordEncoder.encode(signUpRequest.password)

        //Gardener 정보 저장
        val gardener = Gardener(
            ident = signUpRequest.ident,
            passwordHash = passwordHash,
            userName = signUpRequest.userName,
            nickName = "gardener",
            email = signUpRequest.email,
            roles = setOf ("ROLE_USER")
        )

        redisTemplate.opsForHash<String, Any>().put("gardeners", gardener.ident, gardener)

        return Mono.just(SignupResponse(ident = gardener.ident, userName = gardener.userName))
    }

    override fun login(loginRequestDto: LoginRequest): Mono<LoginResponse> {
        // Redis에서 사용자 정보 조회
        val gardener = redisTemplate.opsForHash<String, Any>().get("users", loginRequestDto.ident) as? Gardener

        return if (gardener != null &&
            passwordEncoder.matches(loginRequestDto.password, gardener.passwordHash)
        ) {
            val token = jwtProvider.createToken(gardener.ident, gardener.roles)
            Mono.just(LoginResponse(token = token, ident = gardener.ident, roles = gardener.roles))
        } else {
            Mono.error(IllegalArgumentException("Invalid ident or password"))
        }

    }

    /*
    회원가입할 때 이메일 인증 필요 없음.

    override fun sendEmailMessage(data: EmailSend): String {
    }

    override fun verificationEmail(data: EmailVerification): String {
    }
    */

    //password 유효성 검사
    private fun validatePassword(password: String) {
        val validationResult = PasswordValidator.validate(password)
        if (!validationResult.isValid) {
            throw IllegalArgumentException(validationResult.errors.joinToString("\n"))
        }
    }

}