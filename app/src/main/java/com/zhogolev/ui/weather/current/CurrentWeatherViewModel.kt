package com.zhogolev.ui.weather.current

import androidx.lifecycle.ViewModel
import com.zhogolev.data.repository.ForecastRepository
import com.zhogolev.internal.UnitSystem
import com.zhogolev.internal.lazyDeferred

class CurrentWeatherViewModel(
    forecastRepository: ForecastRepository
) : ViewModel() {
    private val unitSystem = UnitSystem.METRIC
    val isMetric: Boolean
        get() = unitSystem == UnitSystem.METRIC

    val weather by lazyDeferred {
        forecastRepository.getCurrentWeather(isMetric)
    }
}
