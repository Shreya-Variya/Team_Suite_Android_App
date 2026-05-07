package com.example.teamsuite.data.model.leave

data class ApplyForLeaveRequest(
    val employeeId: String,
    val leavePolicyId: String,
    val startDate: String,
    val endDate: String,
    val reason: String,
)
