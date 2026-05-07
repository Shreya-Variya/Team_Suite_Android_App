package com.example.teamsuite.repository.attendance

import com.example.teamsuite.apiobject.ApiObject
import com.example.teamsuite.data.model.attendance.breakout.BreakOutRequest
import com.example.teamsuite.data.model.attendance.breakout.BreakOutResponse

class BreakOutRepository {
    suspend fun breakOut(empid: String, request: BreakOutRequest): retrofit2.Response<BreakOutResponse>{
        return ApiObject.api.breakOut(empid, request)
    }
}