package com.example.teamsuite.repository.employee

import com.example.teamsuite.apiobject.ApiObject
import com.example.teamsuite.data.model.employee.GetSpecificEmployeeResponse

class GetSpecificEmployeeRepository {
    suspend fun getSpecificEmployee(email: String): retrofit2.Response<GetSpecificEmployeeResponse>{
        return ApiObject.api.getSpecificEmployee(email)
    }
}