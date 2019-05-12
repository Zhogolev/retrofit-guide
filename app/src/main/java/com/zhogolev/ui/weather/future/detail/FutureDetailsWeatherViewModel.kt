package com.zhogolev.ui.weather.future.detail

import com.zhogolev.data.provider.UnitProvider
import com.zhogolev.data.repository.ForecastRepository
import com.zhogolev.internal.lazyDeferred
import com.zhogolev.ui.base.WeatherViewModel
import org.threeten.bp.LocalDate

class FutureDetailsWeatherViewModel(
    private val detailDate: LocalDate,
    private val forecastRepository: ForecastRepository,
    unitProvider: UnitProvider
) : WeatherViewModel(forecastRepository,unitProvider) {
    val weather by lazyDeferred {
        forecastRepository.getDetailWeatherByDateAndMetric(detailDate, super.isMetricUnit)
    }
}
