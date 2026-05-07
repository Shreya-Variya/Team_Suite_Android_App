package com.example.teamsuite.repository.attendance

import com.example.teamsuite.apiobject.ApiObject
import com.example.teamsuite.data.model.attendance.attendancereport.AttendanceByDateRequest
import com.example.teamsuite.data.model.attendance.attendancereport.AttendanceByDateResponse

class AttendanceByDateRepository {
    suspend fun attendanceByDate(companyid: String, request: AttendanceByDateRequest): retrofit2.Response<AttendanceByDateResponse>{
        return ApiObject.api.attendanceByDate(companyid, request)
    }
}