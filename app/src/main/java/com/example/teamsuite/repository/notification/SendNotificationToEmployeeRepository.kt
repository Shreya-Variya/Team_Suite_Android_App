package com.example.teamsuite.repository.notification

import com.example.teamsuite.apiobject.ApiObject
import com.example.teamsuite.data.model.notification.SendNotificationToEmployeeRequest
import com.example.teamsuite.data.model.notification.SendNotificationToEmployeeResponse

class SendNotificationToEmployeeRepository {
    suspend fun sendNotificationToEmployee(request: SendNotificationToEmployeeRequest): retrofit2.Response<SendNotificationToEmployeeResponse>{
        return ApiObject.api.sendNotificationToEmployee(request)
    }
}