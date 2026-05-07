package com.example.teamsuite.apiobject

import com.example.teamsuite.apiinterface.ApiInterface
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiObject {
//    const val BASE_URL = "http://10.0.2.2:3000/"
//    const val BASE_URL = "http://192.168.29.13" +
//        ":3000/"
    const val BASE_URL = "https://team-suite-hrms-webapp.onrender.com"

    val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    val api: ApiInterface by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)
    }
}