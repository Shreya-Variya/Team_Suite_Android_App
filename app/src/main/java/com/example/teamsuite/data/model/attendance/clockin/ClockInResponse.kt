package com.example.teamsuite.data.model.attendance.clockin

data class ClockInResponse(
    val success: Boolean,
    val message: String,
    val clockin: ClockInData? = null
)
