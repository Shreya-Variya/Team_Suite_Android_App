package com.example.teamsuite.repository.attendance

import com.example.teamsuite.apiobject.ApiObject
import com.example.teamsuite.data.model.attendance.attendancereport.AttendanceReportResponse

class AttendanceReportRepository {
    suspend fun getAttendanceReport(empid: String): retrofit2.Response<AttendanceReportResponse>{
        return ApiObject.api.getAttendanceReport(empid)
    }
}