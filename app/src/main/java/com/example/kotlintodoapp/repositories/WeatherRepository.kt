package com.example.kotlintodoapp.repositories

import com.example.kotlintodoapp.model.Weather
import com.example.kotlintodoapp.model.WeatherApi
import com.example.kotlintodoapp.repositories.interfaces.BaseApiRepository

class WeatherRepository(private val api: WeatherApi) : BaseApiRepository() {
    suspend fun getWeather(dataType: String, lang: String): Weather? {
        return safeApiCall(
            call = { api.getWeatherAsync(dataType, lang).await() },
            errorMessage = "Error when fetching weather"
        )
    }
}