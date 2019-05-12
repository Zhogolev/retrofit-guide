package com.zhogolev.ui.weather.future.list

import com.zhogolev.data.provider.UnitProvider
import com.zhogolev.data.repository.ForecastRepository
import com.zhogolev.internal.lazyDeferred
import com.zhogolev.ui.base.WeatherViewModel
import org.threeten.bp.LocalDate

class FutureWeatherListViewModel(
    private val forecastRepository: ForecastRepository,
    unitProvider: UnitProvider
) : WeatherViewModel(forecastRepository, unitProvider) {
   val weatherEntries by lazyDeferred {
       forecastRepository.getFutureWeatherList(LocalDate.now(), super.isMetricUnit)
   }
}
