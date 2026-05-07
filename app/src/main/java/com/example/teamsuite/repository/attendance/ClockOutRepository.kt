package com.example.teamsuite.repository.attendance

import com.example.teamsuite.apiobject.ApiObject
import com.example.teamsuite.data.model.attendance.clockout.ClockOutResponse

class ClockOutRepository {
    suspend fun clockOut(empid: String): retrofit2.Response<ClockOutResponse>{
        return ApiObject.api.clockOut(empid)
    }
}