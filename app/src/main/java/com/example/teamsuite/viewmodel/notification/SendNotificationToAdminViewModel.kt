package com.example.teamsuite.viewmodel.notification

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teamsuite.data.model.notification.SendNotificationToAdminRequest
import com.example.teamsuite.data.model.notification.SendNotificationToAdminResponse
import com.example.teamsuite.repository.notification.SendNotificationToAdminRepository
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONObject

class SendNotificationToAdminViewModel(private var repository: SendNotificationToAdminRepository): ViewModel() {
    val sendNotificationResult = MutableLiveData<SendNotificationToAdminResponse>()
    val error = MutableLiveData<String>()
    val loading = MutableLiveData<Boolean>()

    fun sendNotificationToAdmin(request: SendNotificationToAdminRequest){
        viewModelScope.launch {
            loading.value = true
            try{
                val response = repository.sendNotificationToAdmin(request)
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