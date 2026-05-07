package com.example.teamsuite.viewmodel.leave

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.teamsuite.abstractclass.BaseViewModel
import com.example.teamsuite.data.model.leave.GetLeaveReportResponse
import com.example.teamsuite.repository.leave.GetLeaveReportRepository
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONObject

class GetLeaveReportViewModel(private var repository: GetLeaveReportRepository): BaseViewModel() {
    val getLeaveReportResult = MutableLiveData<GetLeaveReportResponse>()
    val getAllLeaveReportResult = MutableLiveData<GetLeaveReportResponse>()
    val getApprovedLeaveReportResult = MutableLiveData<GetLeaveReportResponse>()
    val getPendingLeaveReportResult = MutableLiveData<GetLeaveReportResponse>()
    val getRejectedLeaveReportResult = MutableLiveData<GetLeaveReportResponse>()

    val error = MutableLiveData<String>()
    val loading = MutableLiveData<Boolean>()

    fun getLeaveReport(empid: String){
        viewModelScope.launch {
            setLoading(true)
            loading.value = true
            try{
                val response = repository.getLeaveReport(empid)
                if (response.isSuccessful && response.body() != null){
                    getLeaveReportResult.value = response.body()
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

    fun getAllLeaveRecords(empid: String){
        viewModelScope.launch {
            setLoading(true)
            loading.value = true
            try{
                val response = repository.getAllLeaveRecords(empid)
                if (response.isSuccessful && response.body() != null){
                    getAllLeaveReportResult.value = response.body()
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

    fun getApprovedLeaveRecords(empid: String){
        viewModelScope.launch {
            setLoading(true)
            loading.value = true
            try{
                val response = repository.getApprovedLeaveRecords(empid)
                if (response.isSuccessful && response.body() != null){
                    getApprovedLeaveReportResult.value = response.body()
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

    fun getPendingLeaveRecords(empid: String){
        viewModelScope.launch {
            setLoading(true)
            loading.value = true
            try{
                val response = repository.getPendingLeaveRecords(empid)
                if (response.isSuccessful && response.body() != null){
                    getPendingLeaveReportResult.value = response.body()
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

    fun getRejectedLeaveRecords(empid: String){
        viewModelScope.launch {
            setLoading(true)
            loading.value = true
            try{
                val response = repository.getRejectedLeaveRecords(empid)
                if (response.isSuccessful && response.body() != null){
                    getRejectedLeaveReportResult.value = response.body()
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