package com.example.kotlintodoapp.services

import com.example.kotlintodoapp.model.WeatherApi
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private val httpClient = OkHttpClient().newBuilder().build()

    fun retrofit(baseUrl: String): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .client(httpClient)
        .build()

    val weatherApi: WeatherApi =
        retrofit(baseUrl = "https://data.weather.gov.hk/weatherAPI/opendata/").create(WeatherApi::class.java)
}