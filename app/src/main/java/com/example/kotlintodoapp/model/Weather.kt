package com.example.kotlintodoapp.model

import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

data class Weather(
    val humidity: Humidity,
    val icon: List<Int>,
    val iconUpdateTime: String,
    val minTempFrom00To09: String,
    val rainfall: Rainfall,
    val rainfallFrom00To12: String,
    val rainfallJanuaryToLastMonth: String,
    val rainfallLastMonth: String,
    val tcMessage: String,
    val temperature: Temperature,
    val updateTime: String,
    val uvIndex: Uvindex,
    val warningMessage: String

)

interface WeatherApi {
    @GET("weather.php")
    fun getWeatherAsync(
        @Query("dataType") dataType: String,
        @Query("lang") lang: String
    ): Deferred<Response<Weather>>
}
