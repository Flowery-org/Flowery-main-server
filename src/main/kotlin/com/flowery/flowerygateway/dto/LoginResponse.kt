package com.flowery.flowerygateway.dto
data class LoginResponse (val token: String,
                          val ident: String,
                          val roles: Set<String> // user, admin 구분
)