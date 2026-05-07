package com.example.teamsuite.serviceclass

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.teamsuite.data.model.leave.AcceptRejectLeaveRequest
import com.example.teamsuite.repository.leave.AcceptRejectLeaveRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.app.NotificationManager
import com.example.teamsuite.data.model.notification.SendNotificationToEmployeeRequest
import com.example.teamsuite.repository.notification.SendNotificationToEmployeeRepository

class NotificationActionReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val leaveId = intent?.getStringExtra("leaveId") ?: ""
        val employeeId = intent?.getStringExtra("employeeId") ?: ""
        val notificationId = intent?.getIntExtra("notificationId", -1) ?: -1

        if (notificationId != -1) {
            val notificationManager =
                context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.cancel(notificationId)
        }

        if (leaveId.isEmpty() || employeeId.isEmpty()) return

        when(intent?.action) {
            "ACTION_ACCEPT" -> {
                callAcceptApi(context, leaveId, employeeId)
            }

            "ACTION_REJECT" -> {
                callRejectApi(context, leaveId, employeeId)
            }
        }
    }

    private fun callAcceptApi(context: Context?, leaveId: String, employeeId: String){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repository = AcceptRejectLeaveRepository()
                val response = repository.acceptLeave(AcceptRejectLeaveRequest(leaveId, employeeId))

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && response.body() != null){
                        val result = response.body()!!
                        if (result.success) {
                            Toast.makeText(
                                context,
                                result.message,
                                Toast.LENGTH_SHORT
                            ).show()

                            sendNotificationToEmployee(
                                employeeId,
                                "Leave Approved",
                                "Your leave request has been approved."
                            )
                        } else {
                            Toast.makeText(
                                context,
                                result.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    else {
                        val errorMessage = try {
                            val errorJson = response.errorBody()?.string()
                            org.json.JSONObject(errorJson ?: "").getString("message")
                        } catch (e: Exception) {
                            response.message()
                        }

                        Toast.makeText(
                            context,
                            errorMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        context,
                        e.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun callRejectApi(context: Context?, leaveId: String, employeeId: String){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repository = AcceptRejectLeaveRepository()
                val response = repository.rejectLeave(AcceptRejectLeaveRequest(leaveId, employeeId))

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && response.body() != null){
                        val result = response.body()!!

                        if (result.success) {
                            Toast.makeText(
                                context,
                                result.message,
                                Toast.LENGTH_SHORT
                            ).show()

                            sendNotificationToEmployee(
                                employeeId,
                                "Leave Rejected",
                                "Your leave request has been rejected."
                            )
                        } else {
                            Toast.makeText(
                                context,
                                result.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    else {
                        val errorMessage = try {
                            val errorJson = response.errorBody()?.string()
                            org.json.JSONObject(errorJson ?: "").getString("message")
                        } catch (e: Exception) {
                            response.message()
                        }

                        Toast.makeText(
                            context,
                            errorMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        context,
                        e.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun sendNotificationToEmployee(employeeId: String, title: String, body: String){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repository = SendNotificationToEmployeeRepository()

                repository.sendNotificationToEmployee(SendNotificationToEmployeeRequest(employeeId, title, body, "leave_status"))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}