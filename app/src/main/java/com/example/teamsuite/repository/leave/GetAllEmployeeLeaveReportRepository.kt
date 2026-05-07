package com.example.teamsuite.repository.leave

import com.example.teamsuite.apiobject.ApiObject
import com.example.teamsuite.data.model.leave.GetAllEmployeeLeaveReportResponse
import com.example.teamsuite.data.model.leave.LeaveByDateRequest

class GetAllEmployeeLeaveReportRepository {
    suspend fun getEmployeeLeaveReport(companyid: String): retrofit2.Response<GetAllEmployeeLeaveReportResponse>{
        return ApiObject.api.getEmployeeLeaveReport(companyid)
    }

    suspend fun getAllLeaveRecord(companyid: String): retrofit2.Response<GetAllEmployeeLeaveReportResponse>{
        return ApiObject.api.getAllLeaveRecord(companyid)
    }

    suspend fun getApprovedLeaveRecord(companyid: String): retrofit2.Response<GetAllEmployeeLeaveReportResponse>{
        return ApiObject.api.getApprovedLeaveRecord(companyid)
    }

    suspend fun getPendingLeaveRecord(companyid: String): retrofit2.Response<GetAllEmployeeLeaveReportResponse>{
        return ApiObject.api.getPendingLeaveRecord(companyid)
    }

    suspend fun getRejectedLeaveRecord(companyid: String): retrofit2.Response<GetAllEmployeeLeaveReportResponse>{
        return ApiObject.api.getRejectedLeaveRecord(companyid)
    }

    suspend fun getLeaveByDate(companyid: String, request: LeaveByDateRequest): retrofit2.Response<GetAllEmployeeLeaveReportResponse>{
        return ApiObject.api.getLeaveByDate(companyid, request)
    }
}