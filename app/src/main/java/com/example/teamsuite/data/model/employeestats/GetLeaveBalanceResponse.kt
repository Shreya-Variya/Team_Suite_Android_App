package com.example.teamsuite.data.model.employeestats

data class GetLeaveBalanceResponse(
    val success: Boolean,
    val message: String,
    val leavebalance: Int,
)
