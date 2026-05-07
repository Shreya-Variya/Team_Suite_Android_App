package com.example.teamsuite.repository.forgotpassword

import com.example.teamsuite.apiobject.ApiObject
import com.example.teamsuite.data.model.forgotpassword.ForgotPasswordResponse
import com.example.teamsuite.data.model.forgotpassword.ResetPasswordRequest
import com.example.teamsuite.data.model.forgotpassword.SendCodeRequest
import com.example.teamsuite.data.model.forgotpassword.VerifyCodeRequest

class ForgotPasswordRepository {
    suspend fun sendcode(request: SendCodeRequest): retrofit2.Response<ForgotPasswordResponse>{
        return ApiObject.api.sendcode(request)
    }

    suspend fun verifyCode(request: VerifyCodeRequest): retrofit2.Response<ForgotPasswordResponse>{
        return ApiObject.api.verifyCode(request)
    }

    suspend fun resetPassword(request: ResetPasswordRequest): retrofit2.Response<ForgotPasswordResponse>{
        return ApiObject.api.resetPassword(request)
    }
}