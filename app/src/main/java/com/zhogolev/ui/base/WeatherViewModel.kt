package com.zhogolev.ui.base

import androidx.lifecycle.ViewModel
import com.zhogolev.data.provider.UnitProvider
import com.zhogolev.data.repository.ForecastRepository
import com.zhogolev.internal.UnitSystem
import com.zhogolev.internal.lazyDeferred

abstract class WeatherViewModel(
    private val forecastRepository: ForecastRepository,
    unitProvider: UnitProvider
) : ViewModel() {

    private val unitSystem = unitProvider.getUnitSystem()

    val isMetricUnit: Boolean
        get() = unitSystem == UnitSystem.METRIC

    val weatherLocation by lazyDeferred {
        forecastRepository.getWeatherLocation()
    }
}
