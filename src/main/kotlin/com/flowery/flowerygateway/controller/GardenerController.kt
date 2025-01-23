package com.flowery.flowerygateway.controller

import com.flowery.flowerygateway.dto.LoginRequest
import com.flowery.flowerygateway.dto.SignupRequest
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/")
class GardenerController {

    @PutMapping("/user")
    fun signUp(@RequestBody signUpRequest: SignupRequest): String {

        // GardenerService.createGardener(GardenerRepository)
        return ("회원가입 완료")
    }

    @PostMapping("session/new")
    fun logIn(@RequestBody loginRequest: LoginRequest): String {

        return ("로그인 완료")
    }

}