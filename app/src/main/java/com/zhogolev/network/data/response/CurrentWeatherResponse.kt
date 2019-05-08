package com.zhogolev.network.data.response

import com.google.gson.annotations.SerializedName
import com.zhogolev.db.entity.CurrentWeatherEntry
import com.zhogolev.db.entity.Location


data class CurrentWeatherResponse(

    @SerializedName("current")
    val currentWeatherEntry: CurrentWeatherEntry,
    val location: Location
)