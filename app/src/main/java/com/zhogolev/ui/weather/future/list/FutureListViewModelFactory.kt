package com.zhogolev.ui.weather.future.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zhogolev.data.provider.UnitProvider
import com.zhogolev.data.repository.ForecastRepository

@Suppress("UNCHECKED_CAST")
class FutureListViewModelFactory(
    private val forecastRepository: ForecastRepository,
    private val unitProvider: UnitProvider
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FutureWeatherListViewModel(
            forecastRepository,
            unitProvider
        ) as T
    }
}