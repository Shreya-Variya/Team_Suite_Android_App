package com.example.teamsuite.viewmodel.attendance

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.teamsuite.abstractclass.BaseViewModel
import com.example.teamsuite.data.model.attendance.attendancereport.AttendanceByDateRequest
import com.example.teamsuite.data.model.attendance.attendancereport.AttendanceByDateResponse
import com.example.teamsuite.repository.attendance.AttendanceByDateRepository
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONObject

class AttendanceByDateViewModel(private  var repository: AttendanceByDateRepository): BaseViewModel() {
    val attendanceData = MutableLiveData<AttendanceByDateResponse>()
    val error = MutableLiveData<String>()
    val loading = MutableLiveData<Boolean>()

    fun attendanceByDate(companyid: String, request: AttendanceByDateRequest){
        viewModelScope.launch {
            setLoading(true)
            loading.value = true
            try{
                val response = repository.attendanceByDate(companyid, request)
                if (response.isSuccessful && response.body() != null){
                    attendanceData.value = response.body()
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