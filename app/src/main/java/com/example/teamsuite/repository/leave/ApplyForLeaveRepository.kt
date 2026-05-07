package com.example.teamsuite.repository.leave

import com.example.teamsuite.apiobject.ApiObject
import com.example.teamsuite.data.model.leave.ApplyForLeaveRequest
import com.example.teamsuite.data.model.leave.ApplyForLeaveResponse

class ApplyForLeaveRepository {
    suspend fun applyForLeave(request: ApplyForLeaveRequest): retrofit2.Response<ApplyForLeaveResponse>{
        return ApiObject.api.applyForLeave(request)
    }
}