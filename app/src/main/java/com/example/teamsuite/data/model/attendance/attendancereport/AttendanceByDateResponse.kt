package com.example.teamsuite.data.model.attendance.attendancereport

data class AttendanceByDateResponse(
    val success: Boolean,
    val message: String,
    val data: List<AttendanceByDateData>
)
