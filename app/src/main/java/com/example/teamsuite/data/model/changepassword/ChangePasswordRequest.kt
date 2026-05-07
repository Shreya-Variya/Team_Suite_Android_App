package com.example.teamsuite.data.model.changepassword

data class ChangePasswordRequest(
    val email: String,
    val oldPassword: String,
    val newPassword: String
)
