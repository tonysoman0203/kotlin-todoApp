package com.example.kotlintodoapp.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlintodoapp.model.Weather
import com.example.kotlintodoapp.repositories.WeatherRepository
import com.example.kotlintodoapp.services.ApiClient
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class WeatherViewModel(
    private val weatherRepository: WeatherRepository = WeatherRepository(ApiClient.weatherApi)
) : ViewModel() {
    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    val weatherLiveData = MutableLiveData<Weather>()

    fun fetchWeather(dataType: String, lang: String) {
        scope.launch {
            val weather = weatherRepository.getWeather(dataType, lang)
            weatherLiveData.postValue(weather)
        }
    }

    fun cancelAllRequests() = coroutineContext.cancel()
}