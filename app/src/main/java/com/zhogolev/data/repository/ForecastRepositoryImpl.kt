package com.zhogolev.data.repository

import androidx.lifecycle.LiveData
import com.zhogolev.data.db.CurrentWeatherDao
import com.zhogolev.data.db.WeatherLocationDao
import com.zhogolev.data.db.entity.WeatherLocation
import com.zhogolev.data.db.unitalized.UnitSpecificCurrentWeatherEntry
import com.zhogolev.data.network.WeatherNetworkDataSource
import com.zhogolev.data.network.WeatherNetworkDataSourceImpl
import com.zhogolev.data.network.response.CurrentWeatherResponse
import com.zhogolev.data.provider.LocationProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import org.threeten.bp.ZonedDateTime
import java.util.*

class ForecastRepositoryImpl(
    private val currentWeatherDao: CurrentWeatherDao,
    private val weatherLocationDao: WeatherLocationDao,
    private val locationProvider: LocationProvider,
    private val weatherNetworkDataSourece: WeatherNetworkDataSource
) : ForecastRepository {

    override suspend fun getWeatherLocation(): LiveData<WeatherLocation> {
         return withContext(Dispatchers.IO){
             return@withContext weatherLocationDao.getLocation()
         }
    }

    init {
        weatherNetworkDataSourece.dowloadedCurrentWeather.observeForever { newCurrentWeather ->
            persistFetchedCurrentWeather(newCurrentWeather)
        }
    }
    override suspend fun getCurrentWeather(metric: Boolean): LiveData<out UnitSpecificCurrentWeatherEntry> {
        initWeatherData()
        return withContext(Dispatchers.IO) {
            return@withContext if (metric) currentWeatherDao.getWeatherMetric() else
                currentWeatherDao.getWeatherImperial()
        }
    }

    private fun persistFetchedCurrentWeather(fetchedWeather: CurrentWeatherResponse) {
        GlobalScope.launch(Dispatchers.IO) {
            currentWeatherDao.upsert(fetchedWeather.currentWeatherEntry)
            weatherLocationDao.upsert(fetchedWeather.location)
        }
    }

    private suspend fun initWeatherData() {
        val lastWeatherLocation = weatherLocationDao.getLocation().value

        if(lastWeatherLocation == null || locationProvider.hasLocationChanged(lastWeatherLocation)){
            fetchCurrentWeather()
            return
        }

        if(isFetchCurrentDataIsNead(lastWeatherLocation.zonedDateTime))
            fetchCurrentWeather()
    }

    private suspend fun fetchCurrentWeather(){
        weatherNetworkDataSourece.fetchCurrentWeather(
            location = locationProvider.getPrefferedLocationString(),
            languageCode = Locale.getDefault().language
        )
    }

    private fun isFetchCurrentDataIsNead(lastFetchTime: ZonedDateTime): Boolean {
        val thirtyMinutesAgo = ZonedDateTime.now().minusMinutes(30)
        return lastFetchTime.isBefore(thirtyMinutesAgo)
    }
}
