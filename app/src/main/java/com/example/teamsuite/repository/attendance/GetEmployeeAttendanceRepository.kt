package com.example.teamsuite.repository.attendance

import com.example.teamsuite.apiobject.ApiObject
import com.example.teamsuite.data.model.attendance.attendancereport.GetEmployeeAttendanceResponse

class GetEmployeeAttendanceRepository {
    suspend fun getEmployeeAttendance(companyid: String): retrofit2.Response<GetEmployeeAttendanceResponse>{
        return ApiObject.api.getEmployeeAttendance(companyid)
    }
}