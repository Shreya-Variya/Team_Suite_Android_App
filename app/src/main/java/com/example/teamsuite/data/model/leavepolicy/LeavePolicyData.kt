package com.example.teamsuite.data.model.leavepolicy

data class LeavePolicyData(
    val _id: String,
    val companyId: String,
    val leaveType: String,
    val maxLeavePerYear: Int,
    val maxConsecutiveLeave: Int,
    val createdAt: String,
    val updatedAt: String,
    val __v: Int,
)
