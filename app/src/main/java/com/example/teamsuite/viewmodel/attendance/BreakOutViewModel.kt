package com.example.teamsuite.viewmodel.attendance

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teamsuite.abstractclass.BaseViewModel
import com.example.teamsuite.data.model.attendance.breakout.BreakOutRequest
import com.example.teamsuite.data.model.attendance.breakout.BreakOutResponse
import com.example.teamsuite.data.model.attendance.clockout.ClockOutResponse
import com.example.teamsuite.repository.attendance.BreakOutRepository
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONObject

class BreakOutViewModel(private val repository: BreakOutRepository): BaseViewModel() {
    private val _breakOutResult = MutableLiveData<BreakOutResponse>()
    val breakOutResult: LiveData<BreakOutResponse> = _breakOutResult
    val error = MutableLiveData<String>()
    val loading = MutableLiveData<Boolean>()

    fun breakOut(empid: String, request: BreakOutRequest){
        viewModelScope.launch {
            setLoading(true)
            loading.value = true
            try {
                val response = repository.breakOut(empid, request)
                if (response.isSuccessful && response.body() != null){
                    _breakOutResult.value = response.body()
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