package com.example.teamsuite.repository.notification

import com.example.teamsuite.apiobject.ApiObject
import com.example.teamsuite.data.model.notification.SaveFcmTokenRequest
import com.example.teamsuite.data.model.notification.SaveFcmTokenResponse
import retrofit2.Response

class SaveFcmTokenRepository {
     suspend fun saveFcmToken(request: SaveFcmTokenRequest): Response<SaveFcmTokenResponse> {
        return ApiObject.api.saveFcmToken(request)
    }
}