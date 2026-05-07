package com.example.teamsuite.repository.login

import com.example.teamsuite.apiobject.ApiObject
import com.example.teamsuite.data.model.login.LoginRequest
import com.example.teamsuite.data.model.login.LoginResponse

class LoginRepository {
    suspend fun login(request: LoginRequest): retrofit2.Response<LoginResponse>{
        return ApiObject.api.login(request)
    }
}