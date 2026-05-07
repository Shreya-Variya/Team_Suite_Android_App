package com.example.teamsuite.repository.employee

import com.example.teamsuite.apiobject.ApiObject
import com.example.teamsuite.data.model.employee.GetAllEmployeeResponse

class GetAllEmployeesRepository {
    suspend fun getAllEmployee(companyid: String): retrofit2.Response<GetAllEmployeeResponse>{
        return ApiObject.api.getAllEmployee(companyid)
    }
}