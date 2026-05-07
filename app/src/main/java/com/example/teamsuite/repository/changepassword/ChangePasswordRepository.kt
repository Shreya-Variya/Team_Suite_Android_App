package com.example.teamsuite.repository.changepassword

import com.example.teamsuite.apiobject.ApiObject
import com.example.teamsuite.data.model.changepassword.ChangePasswordRequest
import com.example.teamsuite.data.model.changepassword.ChangePasswordResponse

class ChangePasswordRepository {
    suspend fun changePassword(request: ChangePasswordRequest): retrofit2.Response<ChangePasswordResponse>{
        return ApiObject.api.changePassword(request)
    }
}