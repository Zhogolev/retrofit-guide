package com.zhogolev.data.repository

import androidx.lifecycle.LiveData
import com.zhogolev.data.db.unitalized.UnitSpecificCurrentWeatherEntry

interface ForecastRepository {
    suspend fun getCurrentWeather(metric: Boolean):LiveData<out UnitSpecificCurrentWeatherEntry>
}