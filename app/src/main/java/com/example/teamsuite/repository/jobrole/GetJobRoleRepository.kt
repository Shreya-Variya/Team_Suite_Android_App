package com.example.teamsuite.repository.jobrole

import com.example.teamsuite.apiobject.ApiObject
import com.example.teamsuite.data.model.jobrole.GetJobRoleResponse

class GetJobRoleRepository {
    suspend fun getJobRole(id: String): retrofit2.Response<GetJobRoleResponse>{
        return ApiObject.api.getJobRole(id)
    }
}