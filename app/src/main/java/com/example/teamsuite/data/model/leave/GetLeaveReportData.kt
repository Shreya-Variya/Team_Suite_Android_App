package com.example.teamsuite.data.model.leave

data class GetLeaveReportData(
    val leaveId: String,
    val leaveType: String,
    val startDate: String,
    val endDate: String,
    val reason: String,
    val status: String,
)
