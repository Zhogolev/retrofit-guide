package com.zhogolev.data.network.response


import com.google.gson.annotations.SerializedName
import com.zhogolev.data.db.entity.FutureWeatherEntry

data class ForecastDaysContainer(
    @SerializedName("forecastday")
    val entries: List<FutureWeatherEntry>
)