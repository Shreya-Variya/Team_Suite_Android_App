package com.example.teamsuite.viewmodel.notification

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.teamsuite.abstractclass.BaseViewModel
import com.example.teamsuite.data.model.notification.SaveFcmTokenRequest
import com.example.teamsuite.data.model.notification.SaveFcmTokenResponse
import com.example.teamsuite.repository.notification.SaveFcmTokenRepository
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONObject

class SaveFcmTokenViewModel(private var repository: SaveFcmTokenRepository): BaseViewModel() {
    val saveFcmTokenResult = MutableLiveData<SaveFcmTokenResponse>()
    val error = MutableLiveData<String>()
    val loading = MutableLiveData<Boolean>()

    fun saveFcmToken(request: SaveFcmTokenRequest){
        viewModelScope.launch {
            setLoading(true)
            loading.value = true
            try{
                val response = repository.saveFcmToken(request)
                if (response.isSuccessful && response.body() != null){
                    saveFcmTokenResult.value = response.body()
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