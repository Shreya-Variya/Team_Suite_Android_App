package com.example.teamsuite.data.model.leave

data class AllEmployeeLeaveReportData(
    val leaveId: String,
    val employeeId: String,
    val employeeName: String,
    val leavePolicyName: String,
    val startDate: String,
    val endDate: String,
    val leaveStatus: String,
)
