package com.example.teamsuite.viewmodel.leave

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.teamsuite.abstractclass.BaseViewModel
import com.example.teamsuite.data.model.leave.ApplyForLeaveRequest
import com.example.teamsuite.data.model.leave.ApplyForLeaveResponse
import com.example.teamsuite.repository.leave.ApplyForLeaveRepository
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONObject

class ApplyForLeaveViewModel(private var repository: ApplyForLeaveRepository): BaseViewModel() {
    val applyForLeaveResult = MutableLiveData<ApplyForLeaveResponse>()
    val error = MutableLiveData<String>()
    val loading = MutableLiveData<Boolean>()

    fun applyForLeave(request: ApplyForLeaveRequest){
        viewModelScope.launch {
            setLoading(true)
            loading.value = true
            try{
                val response = repository.applyForLeave(request)
                if (response.isSuccessful && response.body() != null){
                    applyForLeaveResult.value = response.body()
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
            setLoading(false)
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