package com.example.teamsuite.viewmodel.attendance

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.teamsuite.abstractclass.BaseViewModel
import com.example.teamsuite.data.model.attendance.clockin.ClockInResponse
import com.example.teamsuite.repository.attendance.ClockInRepository
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONObject

class ClockInViewModel(private val repository: ClockInRepository): BaseViewModel() {
    private val _clockInResult = MutableLiveData<ClockInResponse>()
    val clockInResult: LiveData<ClockInResponse> = _clockInResult
    val error = MutableLiveData<String>()
    val loading = MutableLiveData<Boolean>()

    fun clockIn(empid: String){
        viewModelScope.launch {
            setLoading(true)
            loading.value = true
            try {
                val response = repository.clockIn(empid)
                if (response.isSuccessful && response.body() != null){
                    _clockInResult.value = response.body()
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