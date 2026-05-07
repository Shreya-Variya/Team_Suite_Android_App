package com.example.teamsuite.viewmodel.leave

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.teamsuite.abstractclass.BaseViewModel
import com.example.teamsuite.data.model.leave.GetAllEmployeeLeaveReportResponse
import com.example.teamsuite.data.model.leave.LeaveByDateRequest
import com.example.teamsuite.repository.leave.GetAllEmployeeLeaveReportRepository
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONObject

class GetAllEmployeeLeaveReportViewModel(private var repository: GetAllEmployeeLeaveReportRepository): BaseViewModel() {
    val getEmployeeLeaveReportResult = MutableLiveData<GetAllEmployeeLeaveReportResponse>()
    val AllLeaveRecordResult = MutableLiveData<GetAllEmployeeLeaveReportResponse>()
    val ApprovedLeaveRecordResult = MutableLiveData<GetAllEmployeeLeaveReportResponse>()
    val PendingLeaveRecordResult = MutableLiveData<GetAllEmployeeLeaveReportResponse>()
    val RejectedLeaveRecordResult = MutableLiveData<GetAllEmployeeLeaveReportResponse>()
    val LeaveByDateResult = MutableLiveData<GetAllEmployeeLeaveReportResponse>()
    val error = MutableLiveData<String>()
    val loading = MutableLiveData<Boolean>()

    fun getEmployeeLeaveReport(companyid: String){
        viewModelScope.launch {
            setLoading(true)
            loading.value = true
            try{
                val response = repository.getEmployeeLeaveReport(companyid)
                if (response.isSuccessful && response.body() != null){
                    getEmployeeLeaveReportResult.value = response.body()
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

    fun getAllLeaveRecord(companyid: String){
        viewModelScope.launch {
            setLoading(true)
            loading.value = true
            try{
                val response = repository.getAllLeaveRecord(companyid)
                if (response.isSuccessful && response.body() != null){
                    AllLeaveRecordResult.value = response.body()
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

    fun getApprovedLeaveRecord(companyid: String){
        viewModelScope.launch {
            setLoading(true)
            loading.value = true
            try{
                val response = repository.getApprovedLeaveRecord(companyid)
                if (response.isSuccessful && response.body() != null){
                    ApprovedLeaveRecordResult.value = response.body()
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

    fun getPendingLeaveRecord(companyid: String){
        viewModelScope.launch {
            setLoading(true)
            loading.value = true
            try{
                val response = repository.getPendingLeaveRecord(companyid)
                if (response.isSuccessful && response.body() != null){
                    PendingLeaveRecordResult.value = response.body()
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

    fun getRejectedLeaveRecord(companyid: String){
        viewModelScope.launch {
            setLoading(true)
            loading.value = true
            try{
                val response = repository.getRejectedLeaveRecord(companyid)
                if (response.isSuccessful && response.body() != null){
                    RejectedLeaveRecordResult.value = response.body()
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

    fun getLeaveByDate(companyid: String, request: LeaveByDateRequest){
        viewModelScope.launch {
            setLoading(true)
            loading.value = true
            try{
                val response = repository.getLeaveByDate(companyid, request)
                if (response.isSuccessful && response.body() != null){
                    LeaveByDateResult.value = response.body()
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