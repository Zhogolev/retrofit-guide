package com.zhogolev.ui.weather.current

import androidx.lifecycle.ViewModel
import com.zhogolev.data.provider.UnitProvider
import com.zhogolev.data.repository.ForecastRepository
import com.zhogolev.internal.UnitSystem
import com.zhogolev.internal.lazyDeferred

class CurrentWeatherViewModel(
    private val forecastRepository: ForecastRepository,
    unitProvider: UnitProvider
) : ViewModel() {

    private val unitSystem = unitProvider.getUnitSystem()

    val isMetric: Boolean
        get() = unitSystem == UnitSystem.METRIC

    val weather by lazyDeferred {
        forecastRepository.getCurrentWeather(isMetric)
    }

    val weatherLocation by lazyDeferred {
        forecastRepository.getWeatherLocation()
    }
}
