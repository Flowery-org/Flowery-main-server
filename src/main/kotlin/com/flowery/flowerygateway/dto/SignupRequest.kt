package com.flowery.flowerygateway.dto

data class SignupRequest (val ident: String,
                     val password: String,
                     val userName: String,
                     val email: String,
                     val nickName: String)