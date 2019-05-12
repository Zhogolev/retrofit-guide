package com.zhogolev.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zhogolev.data.db.entity.CURRENT_WEATHER_ID
import com.zhogolev.data.db.entity.CurrentWeatherEntry
import com.zhogolev.data.db.unitalized.current.ImperialCurrentWeatherEntry
import com.zhogolev.data.db.unitalized.current.MetricCurrentWeatherEntry

@Dao
interface CurrentWeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(weatherEntry: CurrentWeatherEntry)

    @Query(value = "select * from current_weather where id = $CURRENT_WEATHER_ID")
    fun getWeatherMetric(): LiveData<MetricCurrentWeatherEntry>

    @Query(value = "select * from current_weather where id = $CURRENT_WEATHER_ID")
    fun getWeatherImperial(): LiveData<ImperialCurrentWeatherEntry>
}