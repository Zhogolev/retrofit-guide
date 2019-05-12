package com.zhogolev.data.db

import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zhogolev.data.db.entity.FutureWeatherEntry
import com.zhogolev.data.db.unitalized.future.ImperialSimpleFutureWeatherEntry
import com.zhogolev.data.db.unitalized.future.MetricSimpleFutureWeatherEntry
import org.threeten.bp.LocalDate

interface FutureWeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(futureWeatherEntries: List<FutureWeatherEntry>)

    @Query("select date, avgtempC, condition_text, condition_icon  from future_weather where date(date) >= date(:startDate)")
    fun getSimpleWeatherForecastsMetric(startDate: LocalDate): LiveData<List<MetricSimpleFutureWeatherEntry>>

    @Query("select date, avgtempF, condition_text, condition_icon  from future_weather where date(date) >= date(:startDate)")
    fun getSimpleWeatherForecastsImperial(startDate: LocalDate): LiveData<List<ImperialSimpleFutureWeatherEntry>>

    @Query("select count(id) from future_weather where date(date) >= date(:startDate)")
    fun getCountFutureWeather(startDate: LocalDate): Int

    @Query("delete from future_weather where date(date) < date(:firstDateToKeep)")
    fun deleteOldEntries(firstDateToKeep: LocalDate)
}