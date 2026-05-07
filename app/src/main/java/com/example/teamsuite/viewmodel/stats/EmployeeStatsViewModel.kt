package com.example.teamsuite.viewmodel.stats

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.teamsuite.abstractclass.BaseViewModel
import com.example.teamsuite.data.model.employeestats.GetCurrentMonthAttendanceResponse
import com.example.teamsuite.data.model.employeestats.GetLeaveBalanceResponse
import com.example.teamsuite.data.model.employeestats.GetWorkHourResponse
import com.example.teamsuite.repository.stats.EmployeeStatsRepository
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONObject

class EmployeeStatsViewModel(private var repository: EmployeeStatsRepository): BaseViewModel() {
    val getWorkHourResult = MutableLiveData<GetWorkHourResponse>()
    val getCurrentMonthAttendanceResult = MutableLiveData<GetCurrentMonthAttendanceResponse>()
    val getLeaveBalanceResult = MutableLiveData<GetLeaveBalanceResponse>()
    val error = MutableLiveData<String?>()
    val loading = MutableLiveData<Boolean>()

    fun getWorkHour(empid: String){
        viewModelScope.launch {
            setLoading(true)
            loading.value = true
            try{
                val response = repository.getWorkHour(empid)
                if (response.isSuccessful && response.body() != null){
                    getWorkHourResult.value = response.body()
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

    fun getCurrentMonthAttendance(empid: String){
        viewModelScope.launch {
            setLoading(true)
            loading.value = true
            try{
                val response = repository.getCurrentMonthAttendance(empid)
                if (response.isSuccessful && response.body() != null){
                    getCurrentMonthAttendanceResult.value = response.body()
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

    fun getLeaveBalance(empid: String){
        viewModelScope.launch {
            setLoading(true)
            loading.value = true
            try{
                val response = repository.getLeaveBalance(empid)
                if (response.isSuccessful && response.body() != null){
                    getLeaveBalanceResult.value = response.body()
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