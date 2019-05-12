package com.zhogolev.data.repository

import androidx.lifecycle.LiveData
import com.zhogolev.data.db.entity.WeatherLocation
import com.zhogolev.data.db.unitalized.current.UnitSpecificCurrentWeatherEntry
import com.zhogolev.data.db.unitalized.future.detail.UnitSpecificWeatherDetailEntry
import com.zhogolev.data.db.unitalized.future.list.UnitSpecificSimpleFutureWeatherEntry
import org.threeten.bp.LocalDate

interface ForecastRepository {
    suspend fun getCurrentWeather(metric: Boolean):LiveData<out UnitSpecificCurrentWeatherEntry>
    suspend fun getWeatherLocation(): LiveData<WeatherLocation>
    suspend fun getDetailWeatherByDateAndMetric(date: LocalDate, metric: Boolean): LiveData<out UnitSpecificWeatherDetailEntry>
    suspend fun getFutureWeatherList(startDate: LocalDate, metric: Boolean): LiveData<out List<UnitSpecificSimpleFutureWeatherEntry>>
}