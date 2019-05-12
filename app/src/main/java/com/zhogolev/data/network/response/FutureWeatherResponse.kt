package com.zhogolev.data.network.response


import com.google.gson.annotations.SerializedName
import com.zhogolev.data.db.entity.WeatherLocation

data class FutureWeatherResponse(

    @SerializedName("forecast")
    val futureWeatherEntries: ForecastDaysContainer,
    @SerializedName("location")
    val location: WeatherLocation

)