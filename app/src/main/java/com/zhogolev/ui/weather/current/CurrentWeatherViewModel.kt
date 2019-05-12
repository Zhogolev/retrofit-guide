package com.zhogolev.ui.weather.current

import com.zhogolev.data.provider.UnitProvider
import com.zhogolev.data.repository.ForecastRepository
import com.zhogolev.internal.lazyDeferred
import com.zhogolev.ui.base.WeatherViewModel

class CurrentWeatherViewModel(
    private val forecastRepository: ForecastRepository,
    unitProvider: UnitProvider) : WeatherViewModel(forecastRepository, unitProvider)  {
    val weather by lazyDeferred {
        forecastRepository.getCurrentWeather(super.isMetricUnit)
    }

}
