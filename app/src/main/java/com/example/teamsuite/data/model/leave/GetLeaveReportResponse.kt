package com.example.teamsuite.data.model.leave

data class GetLeaveReportResponse(
    val success: Boolean,
    val message: String,
    val data: List<GetLeaveReportData>
)
