package com.flowery.flowerygateway.controller

import com.flowery.flowerygateway.dto.LoginRequest
import com.flowery.flowerygateway.dto.SignupRequest
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/")
class GardenerController {

    @PostMapping("/signup")
    fun signUp(@RequestBody signUpRequest: SignupRequest): String {

        // GardenerService.createGardener(GardenerRepository)
        return ("회원가입 완료")
    }

    @PostMapping("/login")
    fun logIn(@RequestBody loginRequest: LoginRequest): String {

        return ("로그인 완료")
    }

}