package com.example.teamsuite.repository.employee

import com.example.teamsuite.apiobject.ApiObject
import com.example.teamsuite.data.model.employee.UpdateEmployeeResponse
import com.example.teamsuite.data.model.employee.UpdateEmployeeWrapper

class UpdateEmployeeRepository {
    suspend fun updateEmployee(id: String,request: UpdateEmployeeWrapper): retrofit2.Response<UpdateEmployeeResponse>{
        return ApiObject.api.updateEmployee(id, request)
    }
}