package com.zhogolev.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.zhogolev.internal.NoConnectivityException
import com.zhogolev.data.network.response.CurrentWeatherResponse
import com.zhogolev.data.network.response.FutureWeatherResponse



class WeatherNetworkDataSourceImpl(private val apiWeather: ApiWeather) : WeatherNetworkDataSource {

    private val TAG = "Z.NETWORK"

    init {
        Log.d(TAG, "Init weather data source")
    }

    private val _downloadedCurrentWeather = MutableLiveData<CurrentWeatherResponse>()

    override val dowloadedCurrentWeather: LiveData<CurrentWeatherResponse>
        get() = _downloadedCurrentWeather

    override suspend fun fetchCurrentWeather(location: String, languageCode: String) {
       Log.d(TAG, "start fetching data")
       try {
           val fetchedCurrentWeather = apiWeather
               .getCurrentWeather(location, languageCode)
               .await()

           Log.d(TAG, "fetched data = $fetchedCurrentWeather")
           _downloadedCurrentWeather.postValue(fetchedCurrentWeather)
       }catch (e: NoConnectivityException){
           Log.e("Connectivity", "no internet connection")
       }
    }

    private val _downloadedFutureWeather = MutableLiveData<FutureWeatherResponse>()

    override val dowloadedFutureWeather: LiveData<FutureWeatherResponse>
        get() = _downloadedFutureWeather

    override suspend fun fetchFutureWeather(location: String, languageCode: String, days: Int) {
        Log.d(TAG, "start fetching data")
        try {
            val fetchedFutureWeather = apiWeather
                .getFutureWeather(location, languageCode, days)
                .await()

            Log.d(TAG, "fetched data = $fetchedFutureWeather")
            _downloadedFutureWeather.postValue(fetchedFutureWeather)
        }catch (e: NoConnectivityException){
            Log.e("Connectivity", "no internet connection")
        }
    }
}