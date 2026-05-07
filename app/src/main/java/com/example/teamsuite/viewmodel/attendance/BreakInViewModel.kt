package com.example.teamsuite.viewmodel.attendance

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teamsuite.abstractclass.BaseViewModel
import com.example.teamsuite.data.model.attendance.breakin.BreakInResponse
import com.example.teamsuite.data.model.attendance.clockin.ClockInResponse
import com.example.teamsuite.repository.attendance.BreakInRepository
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONObject

class BreakInViewModel(private val repository: BreakInRepository): BaseViewModel() {
    private val _breakInResult = MutableLiveData<BreakInResponse>()
    val breakInResult: LiveData<BreakInResponse> = _breakInResult
    val error = MutableLiveData<String>()
    val loading = MutableLiveData<Boolean>()

    fun breakIn(empid: String){
        viewModelScope.launch {
            setLoading(true)
            loading.value = true
            try{
                val response = repository.breakIn(empid)
                if (response.isSuccessful && response.body() != null){
                    _breakInResult.value = response.body()
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