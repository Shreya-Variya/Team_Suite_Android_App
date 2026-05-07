package com.example.teamsuite.data.model.leavepolicy

data class AddLeavePolicyRequest(
    val companyId: String,
    val leaveType: String,
    val maxLeavePerYear: Int,
    val maxConsecutiveLeave: Int
)
