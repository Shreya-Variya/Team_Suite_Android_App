package com.example.teamsuite.data.model.employee

import com.example.teamsuite.data.model.login.UserData

data class GetAllEmployeeResponse(
    val success: Boolean,
    val message: String,
    val data: List<UserData>
)
