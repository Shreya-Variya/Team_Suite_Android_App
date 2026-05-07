package com.example.teamsuite.data.model.forgotpassword

data class VerifyCodeRequest(
    val email: String,
    val code: String
)
