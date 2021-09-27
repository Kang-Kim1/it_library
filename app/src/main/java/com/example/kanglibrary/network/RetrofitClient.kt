package com.example.kanglibrary.network

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private lateinit var instance : Retrofit

    // Base URL
    const val BASE_URL = "https://api.itbook.store/"


    fun getInstance(): Retrofit {
        if(!::instance.isInitialized) {
            instance = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return instance // Can't be null
    }
}