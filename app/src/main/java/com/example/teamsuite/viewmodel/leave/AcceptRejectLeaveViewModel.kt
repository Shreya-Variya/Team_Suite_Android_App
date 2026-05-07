package com.example.teamsuite.viewmodel.leave

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.teamsuite.abstractclass.BaseViewModel
import com.example.teamsuite.data.model.leave.AcceptRejectLeaveRequest
import com.example.teamsuite.data.model.leave.AcceptRejectLeaveResponse
import com.example.teamsuite.repository.leave.AcceptRejectLeaveRepository
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONObject

class AcceptRejectLeaveViewModel(private var repository: AcceptRejectLeaveRepository): BaseViewModel() {
    val acceptResult = MutableLiveData<AcceptRejectLeaveResponse>()
    val rejectResult = MutableLiveData<AcceptRejectLeaveResponse>()
    val error = MutableLiveData<String>()
    val loading = MutableLiveData<Boolean>()

    fun acceptLeave(request: AcceptRejectLeaveRequest){
        viewModelScope.launch {
            setLoading(true)
            loading.value = true
            try{
                val response = repository.acceptLeave(request)
                if (response.isSuccessful && response.body() != null){
                    acceptResult.value = response.body()
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

    fun rejectLeave(request: AcceptRejectLeaveRequest){
        viewModelScope.launch {
            setLoading(true)
            loading.value = true
            try{
                val response = repository.rejectLeave(request)
                if (response.isSuccessful && response.body() != null){
                    rejectResult.value = response.body()
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