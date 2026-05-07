package com.example.teamsuite.data.model.attendance.breakout

import com.example.teamsuite.data.model.attendance.breakin.BreakInData

data class BreakOutResponse(
    val success: Boolean,
    val message: String,
    val breakout: BreakOutData? = null,
    val breaktime: BreakTimeData? = null
)
