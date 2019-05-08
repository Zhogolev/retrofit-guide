package com.zhogolev.network

import androidx.lifecycle.LiveData
import com.zhogolev.network.data.response.CurrentWeatherResponse

interface WeatherNetworkDataSource {
    val dowloadedCurrentWeather: LiveData<CurrentWeatherResponse>

    suspend fun fetchCurrentWeather(
        location: String,
        languageCode: String
    )
}