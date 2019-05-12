package com.zhogolev.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zhogolev.data.db.entity.CURRENT_WEATHER_ID
import com.zhogolev.data.db.entity.WeatherLocation

@Dao
interface WeatherLocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(weatherLocation: WeatherLocation)

    @Query("select * from weather_location where id = $CURRENT_WEATHER_ID")
    fun getLocation(): LiveData<WeatherLocation>

    @Query("select * from weather_location where id = $CURRENT_WEATHER_ID")
    fun getLocationNonLive(): WeatherLocation?
}