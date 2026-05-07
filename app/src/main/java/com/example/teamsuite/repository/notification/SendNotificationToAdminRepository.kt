package com.example.teamsuite.repository.notification

import com.example.teamsuite.apiobject.ApiObject
import com.example.teamsuite.data.model.notification.SendNotificationToAdminRequest
import com.example.teamsuite.data.model.notification.SendNotificationToAdminResponse

class SendNotificationToAdminRepository {
    suspend fun sendNotificationToAdmin(request: SendNotificationToAdminRequest): retrofit2.Response<SendNotificationToAdminResponse>{
        return ApiObject.api.sendNotificationToAdmin(request)
    }
}