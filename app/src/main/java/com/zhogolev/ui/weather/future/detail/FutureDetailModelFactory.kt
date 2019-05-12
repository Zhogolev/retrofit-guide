package com.zhogolev.ui.weather.future.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zhogolev.data.provider.UnitProvider
import com.zhogolev.data.repository.ForecastRepository
import org.threeten.bp.LocalDate

@Suppress("UNCHECKED_CAST")
class FutureDetailModelFactory(
    private val detailDate: LocalDate,
    private val forecastRepository: ForecastRepository,
    private val unitProvider: UnitProvider
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FutureDetailsWeatherViewModel(
            detailDate,
            forecastRepository,
            unitProvider
        ) as T
    }
}