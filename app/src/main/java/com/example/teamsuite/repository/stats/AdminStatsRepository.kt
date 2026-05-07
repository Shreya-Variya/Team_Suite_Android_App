package com.example.teamsuite.repository.stats

import com.example.teamsuite.apiobject.ApiObject
import com.example.teamsuite.data.model.adminstats.GetTotalEmployeesResponse
import com.example.teamsuite.data.model.adminstats.OnLeaveEmployeesResponse

class AdminStatsRepository {
    suspend fun getTotalEmployees(companyid: String): retrofit2.Response<GetTotalEmployeesResponse>{
        return ApiObject.api.getTotalEmployees(companyid)
    }

    suspend fun onLeaveEmployees(companyid: String): retrofit2.Response<OnLeaveEmployeesResponse>{
        return ApiObject.api.onLeaveEmployees(companyid)
    }
}