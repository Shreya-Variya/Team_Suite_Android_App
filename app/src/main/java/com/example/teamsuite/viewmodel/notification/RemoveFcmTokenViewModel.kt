package com.example.teamsuite.viewmodel.notification

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.teamsuite.abstractclass.BaseViewModel
import com.example.teamsuite.data.model.notification.RemoveFcmTokenResponse
import com.example.teamsuite.repository.notification.RemoveFcmTokenRepository
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONObject

class RemoveFcmTokenViewModel(private var repository: RemoveFcmTokenRepository): BaseViewModel() {
    val removeFcmTokenResult = MutableLiveData<RemoveFcmTokenResponse>()
    val error = MutableLiveData<String>()
    val loading = MutableLiveData<Boolean>()

    fun removeFcmToken(empid: String){
        viewModelScope.launch {
            setLoading(true)
            loading.value = true
            try{
                val response = repository.removeFcmToken(empid)
                if (response.isSuccessful && response.body() != null){
                    removeFcmTokenResult.value = response.body()
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