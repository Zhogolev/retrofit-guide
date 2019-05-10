package com.zhogolev.data.network

import androidx.lifecycle.LiveData
import com.zhogolev.data.network.response.CurrentWeatherResponse

interface WeatherNetworkDataSource {
    val dowloadedCurrentWeather: LiveData<CurrentWeatherResponse>

    suspend fun fetchCurrentWeather(
        location: String,
        languageCode: String
    )
}