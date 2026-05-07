package com.example.teamsuite.repository.department

import com.example.teamsuite.apiobject.ApiObject
import com.example.teamsuite.data.model.department.GetDepartmentResponse

class GetDepartmentRepository {
    suspend fun getDepartment(): retrofit2.Response<GetDepartmentResponse>{
        return ApiObject.api.getDepartment()
    }
}