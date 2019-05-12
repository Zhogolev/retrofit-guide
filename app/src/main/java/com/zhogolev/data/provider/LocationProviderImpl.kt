package com.zhogolev.data.provider

import com.zhogolev.data.db.entity.WeatherLocation

class LocationProviderImpl : LocationProvider {
    override suspend fun hasLocationChanged(lastWeatherLocation: WeatherLocation): Boolean {
        return true
    }

    override suspend fun getPrefferedLocationString(): String {
       return "Los Angeles"
    }
}