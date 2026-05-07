package com.example.teamsuite.repository.leavepolicy

import com.example.teamsuite.apiobject.ApiObject
import com.example.teamsuite.data.model.leavepolicy.AddLeavePolicyResponse
import com.example.teamsuite.data.model.leavepolicy.AddLeavePolicyWrapper

class AddLeavePolicyRepository {
    suspend fun addLeavePolicy(request: AddLeavePolicyWrapper): retrofit2.Response<AddLeavePolicyResponse>{
        return ApiObject.api.addLeavePolicy(request)
    }
}