package com.example.teamsuite.data.model.login

data class LoginResponse(
    val success: Boolean,
    val message: String,
    val userData: UserData? = null
)
