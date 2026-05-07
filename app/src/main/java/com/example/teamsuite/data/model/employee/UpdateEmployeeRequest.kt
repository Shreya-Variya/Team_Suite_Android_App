package com.example.teamsuite.data.model.employee

import com.example.teamsuite.data.model.login.Address

data class UpdateEmployeeRequest(
    val employeeId: String,
    val employeeName: String,
    val dob: String,
    val gender: String,
    val maritalStatus: Boolean,
    val email: String,
    val mobileNo: String,
    val address: Address,
    val education: String,
    val experience: String,
    val department: String,
    val jobRole: String,
    val joinDate: String,
    val companyId: String
)
