package com.example.teamsuite.viewmodel.leavepolicy

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.teamsuite.abstractclass.BaseViewModel
import com.example.teamsuite.data.model.leavepolicy.GetLeavePolicyResponse
import com.example.teamsuite.repository.leavepolicy.GetLeavePolicyRepository
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONObject

class GetLeavePolicyViewModel(private var repository: GetLeavePolicyRepository): BaseViewModel() {
    val leavePolicyData = MutableLiveData<GetLeavePolicyResponse>()
    val error = MutableLiveData<String>()
    val loading = MutableLiveData<Boolean>()

    fun getLeavePolicy(companyid: String){
        viewModelScope.launch {
            setLoading(true)
            loading.value = true
            try{
                val response = repository.getLeavePolicy(companyid)
                if (response.isSuccessful && response.body() != null){
                    leavePolicyData.value = response.body()
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