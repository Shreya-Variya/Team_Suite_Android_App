package com.example.teamsuite.data.model.attendance.breakin

import com.example.teamsuite.data.model.attendance.clockin.ClockInData

data class BreakInResponse(
    val success: Boolean,
    val message: String,
    val breakin: BreakInData? = null
)
