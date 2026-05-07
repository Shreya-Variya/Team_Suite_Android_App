package com.example.teamsuite.viewmodel.notification

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teamsuite.data.model.notification.SendNotificationToEmployeeRequest
import com.example.teamsuite.data.model.notification.SendNotificationToEmployeeResponse
import com.example.teamsuite.repository.notification.SendNotificationToEmployeeRepository
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONObject

class SendNotificationToEmployeeViewModel(private var repository: SendNotificationToEmployeeRepository): ViewModel() {
    val sendNotificationResult = MutableLiveData<SendNotificationToEmployeeResponse>()
    val error = MutableLiveData<String>()
    val loading = MutableLiveData<Boolean>()

    fun sendNotificationToEmployee(request: SendNotificationToEmployeeRequest){
        viewModelScope.launch {
            loading.value = true
            try{
                val response = repository.sendNotificationToEmployee(request)
                if (response.isSuccessful && response.body() != null){
                    sendNotificationResult.value = response.body()
                }
                else{
                    val errMsg = parseErrorMessage(response.errorBody())
                    error.value = errMsg
                }
            }
            catch (e: Exception){
                Log.i("ERROR", "error: ${e}")
                error.value = "Internet Connection Failed."
            }
            loading.value = false
        }
    }

    private fun parseErrorMessage(errorBody: ResponseBody?): String {
        return try{
            errorBody?.string()?.let { errorString ->
                val json = JSONObject(errorString)
                json.optString("message","Unknown error")
            }?: "Unknown error"
        }
        catch (e: Exception){
            "Unknown error"
        }
    }
}