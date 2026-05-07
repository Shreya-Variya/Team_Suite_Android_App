package com.example.teamsuite.data.model.forgotpassword

data class ResetPasswordRequest(
    val email: String,
    val newPassword: String
)
