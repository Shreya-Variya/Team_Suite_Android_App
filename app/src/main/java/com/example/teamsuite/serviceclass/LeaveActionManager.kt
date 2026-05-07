package com.example.teamsuite.serviceclass

import android.content.Context
import android.widget.Toast
import com.example.teamsuite.data.model.leave.AcceptRejectLeaveRequest
import com.example.teamsuite.data.model.notification.SendNotificationToEmployeeRequest
import com.example.teamsuite.repository.leave.AcceptRejectLeaveRepository
import com.example.teamsuite.repository.notification.SendNotificationToEmployeeRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LeaveActionManager {
    fun callAcceptApi(context: Context?, leaveId: String, employeeId: String, onSuccess: (() -> Unit)? = null, onError: ((String) -> Unit)? = null){
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
                            onSuccess?.invoke()
                        } else {
                            onError?.invoke(result.message)
//                            Toast.makeText(
//                                context,
//                                result.message,
//                                Toast.LENGTH_SHORT
//                            ).show()
                        }
                    }
                    else {
                        val errorMessage = try {
                            val errorJson = response.errorBody()?.string()
                            org.json.JSONObject(errorJson ?: "").getString("message")
                        } catch (e: Exception) {
                            response.message()
                        }

                        onError?.invoke(errorMessage)
//                        Toast.makeText(
//                            context,
//                            errorMessage,
//                            Toast.LENGTH_SHORT
//                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
//                    Toast.makeText(
//                        context,
//                        e.message,
//                        Toast.LENGTH_SHORT
//                    ).show()
                    onError?.invoke(e.message ?: "Something went wrong")
                }
            }
        }
    }

    fun callRejectApi(context: Context?, leaveId: String, employeeId: String, onSuccess: (() -> Unit)? = null, onError: ((String) -> Unit)? = null){
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
                            onSuccess?.invoke()
                        } else {
//                            Toast.makeText(
//                                context,
//                                result.message,
//                                Toast.LENGTH_SHORT
//                            ).show()
                            onError?.invoke(result.message)
                        }
                    }
                    else {
                        val errorMessage = try {
                            val errorJson = response.errorBody()?.string()
                            org.json.JSONObject(errorJson ?: "").getString("message")
                        } catch (e: Exception) {
                            response.message()
                        }

//                        Toast.makeText(
//                            context,
//                            errorMessage,
//                            Toast.LENGTH_SHORT
//                        ).show()
                        onError?.invoke(errorMessage)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
//                    Toast.makeText(
//                        context,
//                        e.message,
//                        Toast.LENGTH_SHORT
//                    ).show()
                    onError?.invoke(e.message ?: "Something went wrong")
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