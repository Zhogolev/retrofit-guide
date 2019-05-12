package com.zhogolev.data.provider

import com.zhogolev.data.db.entity.WeatherLocation

interface LocationProvider {

    suspend fun hasLocationChanged(lastWeatherLocation: WeatherLocation): Boolean

    suspend fun getPrefferedLocationString(): String
}