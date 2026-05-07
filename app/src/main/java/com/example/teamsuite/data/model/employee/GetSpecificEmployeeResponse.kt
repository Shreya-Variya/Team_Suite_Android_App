package com.example.teamsuite.data.model.employee

import com.example.teamsuite.data.model.login.UserData

data class GetSpecificEmployeeResponse(
    val success: Boolean,
    val message: String,
    val employeeData: UserData
)
