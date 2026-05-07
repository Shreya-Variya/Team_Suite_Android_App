package com.example.teamsuite.repository.employee

import com.example.teamsuite.apiobject.ApiObject
import com.example.teamsuite.data.model.employee.DeleteEmployeeResponse

class DeleteEmployeeRepository {
    suspend fun deleteEmployee(id: String): retrofit2.Response<DeleteEmployeeResponse>{
        return ApiObject.api.deleteEmployee(id)
    }
}