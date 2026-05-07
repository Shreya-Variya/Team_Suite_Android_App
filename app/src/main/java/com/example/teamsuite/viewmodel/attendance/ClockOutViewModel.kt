package com.example.teamsuite.viewmodel.attendance

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teamsuite.abstractclass.BaseViewModel
import com.example.teamsuite.data.model.attendance.clockout.ClockOutResponse
import com.example.teamsuite.repository.attendance.ClockOutRepository
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONObject

class ClockOutViewModel(private val repository: ClockOutRepository): BaseViewModel() {
    private val _clockOutResult = MutableLiveData<ClockOutResponse>()
    val clockOutResult: LiveData<ClockOutResponse> = _clockOutResult
    val error = MutableLiveData<String>()
    val loading = MutableLiveData<Boolean>()

    fun clockOut(empid: String){
        viewModelScope.launch {
            setLoading(true)
            loading.value = true
            try {
                val response = repository.clockOut(empid)
                if (response.isSuccessful && response.body() != null){
                    _clockOutResult.value = response.body()
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