package com.example.teamsuite.repository.employee

import com.example.teamsuite.apiobject.ApiObject
import com.example.teamsuite.data.model.employee.AddEmployeeResponse
import com.example.teamsuite.data.model.employee.AddEmployeeWrapper

class AddEmployeeRepository {
    suspend fun addEmployee(request: AddEmployeeWrapper): retrofit2.Response<AddEmployeeResponse>{
        return ApiObject.api.addEmployee(request)
    }
}