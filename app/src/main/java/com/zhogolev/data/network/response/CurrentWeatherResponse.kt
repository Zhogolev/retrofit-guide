package com.zhogolev.data.network.response

import com.google.gson.annotations.SerializedName
import com.zhogolev.data.db.entity.CurrentWeatherEntry
import com.zhogolev.data.db.entity.WeatherLocation


data class CurrentWeatherResponse(

    @SerializedName("current")
    val currentWeatherEntry: CurrentWeatherEntry,
    val location: WeatherLocation
)