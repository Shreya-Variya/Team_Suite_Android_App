package com.example.teamsuite.data.model.leave

data class GetAllEmployeeLeaveReportResponse(
    val success: Boolean,
    val message: String,
    val data: List<AllEmployeeLeaveReportData>,
)
