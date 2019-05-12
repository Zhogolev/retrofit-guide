package com.zhogolev.data.repository

import androidx.lifecycle.LiveData
import com.zhogolev.data.db.CurrentWeatherDao
import com.zhogolev.data.db.FutureWeatherDao
import com.zhogolev.data.db.WeatherLocationDao
import com.zhogolev.data.db.entity.WeatherLocation
import com.zhogolev.data.db.unitalized.current.UnitSpecificCurrentWeatherEntry
import com.zhogolev.data.db.unitalized.future.UnitSpecificSimpleFutureWeatherEntry
import com.zhogolev.data.network.WeatherNetworkDataSource
import com.zhogolev.data.network.response.CurrentWeatherResponse
import com.zhogolev.data.network.response.FutureWeatherResponse
import com.zhogolev.data.provider.LocationProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.LocalDate
import org.threeten.bp.ZonedDateTime
import java.util.*

private const val FUTURE_WEATHER_DAYS = 7

class ForecastRepositoryImpl(
    private val currentWeatherDao: CurrentWeatherDao,
    private val weatherLocationDao: WeatherLocationDao,
    private val futureWeatherDao: FutureWeatherDao,
    private val locationProvider: LocationProvider,
    private val weatherNetworkDataSourece: WeatherNetworkDataSource
) : ForecastRepository {

    override suspend fun getWeatherLocation(): LiveData<WeatherLocation> {
         return withContext(Dispatchers.IO){
             return@withContext weatherLocationDao.getLocation()
         }
    }

    init {
        weatherNetworkDataSourece.apply {
            dowloadedCurrentWeather.observeForever { newCurrentWeather ->
                persistFetchedCurrentWeather(newCurrentWeather)
            }

            dowloadedFutureWeather.observeForever { newFutureWeather ->
                persistFetchedFutureWeather(newFutureWeather)
            }
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

    override suspend fun getFutureWeatherList(
        startDate: LocalDate,
        metric: Boolean
    ): LiveData<out List<UnitSpecificSimpleFutureWeatherEntry>> {
        return withContext(Dispatchers.IO){
            initWeatherData()
            return@withContext if (metric) futureWeatherDao.getSimpleWeatherForecastsMetric(startDate)
            else futureWeatherDao.getSimpleWeatherForecastsImperial(startDate)
        }
    }

    private fun persistFetchedFutureWeather(fetchResponse: FutureWeatherResponse) {
        fun deleteOldForecastData(){
            val thisMoment = LocalDate.now()
            futureWeatherDao.deleteOldEntries(thisMoment)
        }
        GlobalScope.launch(Dispatchers.IO) {
            deleteOldForecastData()
            futureWeatherDao.insert(fetchResponse.futureWeatherEntries.entries)
            weatherLocationDao.upsert(fetchResponse.location)
        }
    }

    private suspend fun initWeatherData() {
        val lastWeatherLocation = weatherLocationDao.getLocationNonLive()

        if(lastWeatherLocation == null || locationProvider.hasLocationChanged(lastWeatherLocation)){
            fetchCurrentWeather()
            fetchFutureWeather()
            return
        }

        if(isFetchCurrentDataIsNead(lastWeatherLocation.zonedDateTime))
            fetchCurrentWeather()
        
        if(isFetchFutureWeatherNeaded(lastWeatherLocation.zonedDateTime))
            fetchFutureWeather()
    }

    private suspend fun fetchFutureWeather() {
        weatherNetworkDataSourece.fetchFutureWeather(
            location = locationProvider.getPrefferedLocationString(),
            languageCode = Locale.getDefault().language,
            days = FUTURE_WEATHER_DAYS
        )
    }

    private fun isFetchFutureWeatherNeaded(zonedDateTime: ZonedDateTime): Boolean {
        val thisMoment = LocalDate.now()
        val futureWeatherCount = futureWeatherDao.getCountFutureWeather(thisMoment)
        return futureWeatherCount < FUTURE_WEATHER_DAYS
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
