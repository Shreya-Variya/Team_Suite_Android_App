package com.example.teamsuite.repository.leave

import com.example.teamsuite.apiobject.ApiObject
import com.example.teamsuite.data.model.leave.GetLeaveReportResponse

class GetLeaveReportRepository {
    suspend fun getLeaveReport(empid: String): retrofit2.Response<GetLeaveReportResponse>{
        return ApiObject.api.getLeaveReport(empid)
    }

    suspend fun getAllLeaveRecords(empid: String): retrofit2.Response<GetLeaveReportResponse>{
        return ApiObject.api.getAllLeaveRecords(empid)
    }

    suspend fun getApprovedLeaveRecords(empid: String): retrofit2.Response<GetLeaveReportResponse>{
        return ApiObject.api.getApprovedLeaveRecords(empid)
    }

    suspend fun getPendingLeaveRecords(empid: String): retrofit2.Response<GetLeaveReportResponse>{
        return ApiObject.api.getPendingLeaveRecords(empid)
    }

    suspend fun getRejectedLeaveRecords(empid: String): retrofit2.Response<GetLeaveReportResponse>{
        return ApiObject.api.getRejectedLeaveRecords(empid)
    }
}