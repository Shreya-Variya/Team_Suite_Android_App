package com.example.teamsuite.repository.attendance

import com.example.teamsuite.apiobject.ApiObject
import com.example.teamsuite.data.model.attendance.breakin.BreakInResponse

class BreakInRepository {
    suspend fun breakIn(empid: String): retrofit2.Response<BreakInResponse>{
        return ApiObject.api.breakIn(empid)
    }
}