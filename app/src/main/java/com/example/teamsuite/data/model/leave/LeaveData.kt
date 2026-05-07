package com.example.teamsuite.data.model.leave

data class LeaveData(
    val employeeId: String,
    val leavePolicyId: String,
    val startDate: String,
    val endDate: String,
    val reason: String,
    val status: String,
    val _id: String,
    val __v: Int,
)
