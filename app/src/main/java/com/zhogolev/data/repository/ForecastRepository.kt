package com.zhogolev.data.repository

import androidx.lifecycle.LiveData
import com.zhogolev.data.db.entity.WeatherLocation
import com.zhogolev.data.db.unitalized.current.UnitSpecificCurrentWeatherEntry

interface ForecastRepository {
    suspend fun getCurrentWeather(metric: Boolean):LiveData<out UnitSpecificCurrentWeatherEntry>
    suspend fun getWeatherLocation(): LiveData<WeatherLocation>
}