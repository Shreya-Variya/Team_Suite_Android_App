package com.example.teamsuite.repository.notification

import com.example.teamsuite.apiobject.ApiObject
import com.example.teamsuite.data.model.notification.RemoveFcmTokenResponse

class RemoveFcmTokenRepository {
    suspend fun removeFcmToken(empid: String): retrofit2.Response<RemoveFcmTokenResponse>{
        return ApiObject.api.removeFcmToken(empid)
    }
}