package com.flowery.flowerygateway.service
import com.flowery.flowerygateway.dto.*
import com.flowery.flowerygateway.jwt.JwtProvider
import com.flowery.flowerygateway.utils.*
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
@Service
class AuthServiceImpl (
    private val jwtProvider: JwtProvider,
    private val passwordEncoder: PasswordEncoder,
    private val redisTemplate: RedisTemplate<String, Any>,
    private val idValidator: IdValidator,
    private val nickNameValidator: NickNameValidator,
    private val emailValidator: EmailValidator
) : AuthService {

    override fun signUp(signUpRequest: SignupRequest): Mono<SignupResponse> {

        validateName(signUpRequest.userName)// userName 유효성 검사
        validateIdent(signUpRequest.ident)//ident 유효성 검사
        validateNickName(signUpRequest.nickName)//nickName 유효성 검사
        validateEmail(signUpRequest.email)//email 유효성 검사
        validatePassword(signUpRequest.password)//password 유효성 검사
        validatePasswordConfirmation(signUpRequest.password, signUpRequest.passwordConfirmation)//passwordConfirmation = password 인지 확인
        //password Hashing

        val passwordHash = passwordEncoder.encode(signUpRequest.password)

        //Gardener 정보 저장
        val gardener = Gardener(
            ident = signUpRequest.ident,
            passwordHash = passwordHash,
            userName = signUpRequest.userName,
            nickName = signUpRequest.nickName,
            email = signUpRequest.email,
            roles = setOf ("ROLE_USER")
        )

        redisTemplate.opsForHash<String, Any>().put("gardeners", gardener.ident, gardener)
        redisTemplate.opsForHash<String, Any>().put("nickNames", gardener.nickName, true)
        redisTemplate.opsForHash<String, Any>().put("emails", gardener.email, true)
        return Mono.just(SignupResponse(ident = gardener.ident, userName = gardener.userName))
    }


    override fun login(loginRequest: LoginRequest): Mono<LoginResponse> {
        // Redis에서 사용자 정보 조회
        val gardener = redisTemplate.opsForHash<String, Any>().get("users", loginRequest.ident) as? Gardener
        return if (gardener != null &&
            passwordEncoder.matches(loginRequest.password, gardener.passwordHash)
        ) {
            val token = jwtProvider.createToken(gardener.ident, gardener.roles)
            Mono.just(LoginResponse(token = token, ident = gardener.ident, roles = gardener.roles))
        } else {
            Mono.error(IllegalArgumentException("Invalid ident or password"))
        }
    }

    //userName 유효성 검사
    private fun validateName(userName: String) {
        val validationResult = UserNameValidator.validate(userName)
        if (!validationResult.isValid) {
            throw IllegalArgumentException(validationResult.errors.joinToString("\n"))
        }
    }
    //ident 유효성 검사
    private fun validateIdent(ident: String) {
        val validationResult = idValidator.validate(ident) // 의존성으로 주입된 idValidator 사용
        if (!validationResult.isValid) {
            throw IllegalArgumentException(validationResult.errors.joinToString("\n"))
        }
    }
    //password 유효성 검사
    private fun validatePassword(password: String) {
        val validationResult = PasswordValidator.validate(password)
        if (!validationResult.isValid) {
            throw IllegalArgumentException(validationResult.errors.joinToString("\n"))
        }
    }
    // 비밀번호 확인
    private fun validatePasswordConfirmation(password: String, passwordConfirmation: String) {
        if (password != passwordConfirmation) {
            throw IllegalArgumentException("Passwords do not match.")
        }
    }
    //nickName 유효성 검사
    private fun validateNickName(nickName: String) {
        val validationResult = nickNameValidator.validate(nickName)
        if (!validationResult.isValid) {
            throw IllegalArgumentException(validationResult.errors.joinToString("\n"))
        }
    }
    //email 유효성 검사
    private fun validateEmail(email: String) {
        val validationResult = emailValidator.validate(email)
        if (!validationResult.isValid) {
            throw IllegalArgumentException(validationResult.errors.joinToString("\n"))
        }
    }
}