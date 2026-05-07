package com.example.teamsuite.repository.leave

import com.example.teamsuite.apiobject.ApiObject
import com.example.teamsuite.data.model.leave.AcceptRejectLeaveRequest
import com.example.teamsuite.data.model.leave.AcceptRejectLeaveResponse

class AcceptRejectLeaveRepository {
    suspend fun acceptLeave(request: AcceptRejectLeaveRequest): retrofit2.Response<AcceptRejectLeaveResponse>{
        return ApiObject.api.acceptLeave(request)
    }

    suspend fun rejectLeave(request: AcceptRejectLeaveRequest): retrofit2.Response<AcceptRejectLeaveResponse>{
        return ApiObject.api.rejectLeave(request)
    }
}