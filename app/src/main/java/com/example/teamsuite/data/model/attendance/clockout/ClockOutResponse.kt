package com.example.teamsuite.data.model.attendance.clockout

data class ClockOutResponse(
    val success: Boolean,
    val message: String,
    val clockout: ClockOutData? = null,
    val clockoutHistory: ClockOutHistoryData? = null
)
