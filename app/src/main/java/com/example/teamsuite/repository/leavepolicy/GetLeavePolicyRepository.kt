package com.example.teamsuite.repository.leavepolicy

import com.example.teamsuite.apiobject.ApiObject
import com.example.teamsuite.data.model.leavepolicy.GetLeavePolicyResponse

class GetLeavePolicyRepository {
    suspend fun getLeavePolicy(companyid: String): retrofit2.Response<GetLeavePolicyResponse>{
        return ApiObject.api.getLeavePolicy(companyid)
    }
}