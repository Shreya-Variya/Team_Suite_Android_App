package com.example.teamsuite.data.model.leave

import com.example.teamsuite.data.model.login.Address

data class AdminData(
    val address: Address,
    val _id: String,
    val employeeId: String,
    val employeeName: String,
    val dob: String,
    val gender: String,
    val maritalStatus: Boolean,
    val email: String,
    val mobileNo: String,
    val education: String,
    val experience: Double,
    val department: String,
    val jobRole: String,
    val joinDate: String,
    val category: String,
    val companyId: String,
    val companyName: String,
    val __v: Int
)
