package com.example.teamsuite.repository.stats

import com.example.teamsuite.apiobject.ApiObject
import com.example.teamsuite.data.model.employeestats.GetCurrentMonthAttendanceResponse
import com.example.teamsuite.data.model.employeestats.GetLeaveBalanceResponse
import com.example.teamsuite.data.model.employeestats.GetWorkHourResponse

class EmployeeStatsRepository {
    suspend fun getWorkHour(empid: String): retrofit2.Response<GetWorkHourResponse>{
        return ApiObject.api.getWorkHour(empid)
    }

    suspend fun getCurrentMonthAttendance(empid: String): retrofit2.Response<GetCurrentMonthAttendanceResponse>{
        return ApiObject.api.getCurrentMonthAttendance(empid)
    }

    suspend fun getLeaveBalance(empid: String): retrofit2.Response<GetLeaveBalanceResponse>{
        return ApiObject.api.getLeaveBalance(empid)
    }
}