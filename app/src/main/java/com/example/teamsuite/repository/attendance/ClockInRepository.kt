package com.example.teamsuite.repository.attendance

import com.example.teamsuite.apiobject.ApiObject
import com.example.teamsuite.data.model.attendance.clockin.ClockInResponse

class ClockInRepository {
    suspend fun clockIn(empid: String): retrofit2.Response<ClockInResponse>{
        return ApiObject.api.clockIn(empid)
    }
}