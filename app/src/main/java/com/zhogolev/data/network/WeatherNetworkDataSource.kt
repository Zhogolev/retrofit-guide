package com.zhogolev.data.network

import androidx.lifecycle.LiveData
import com.zhogolev.data.network.response.CurrentWeatherResponse
import com.zhogolev.data.network.response.FutureWeatherResponse

interface WeatherNetworkDataSource {
    val dowloadedCurrentWeather: LiveData<CurrentWeatherResponse>
    val dowloadedFutureWeather: LiveData<FutureWeatherResponse>

    suspend fun fetchCurrentWeather(
        location: String,
        languageCode: String
    )

    suspend fun fetchFutureWeather(
        location: String,
        languageCode: String,
        days: Int
    )
}