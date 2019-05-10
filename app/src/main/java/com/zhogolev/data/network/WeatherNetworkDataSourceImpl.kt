package com.zhogolev.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.zhogolev.internal.NoConnectivityException
import com.zhogolev.data.network.response.CurrentWeatherResponse

class WeatherNetworkDataSourceImpl(private val apiWeather: ApiWeather) : WeatherNetworkDataSource {

    private val _downloadedCurrentWeather = MutableLiveData<CurrentWeatherResponse>()

    override val dowloadedCurrentWeather: LiveData<CurrentWeatherResponse>
        get() = _downloadedCurrentWeather

    override suspend fun fetchCurrentWeather(location: String, languageCode: String) {
       try {
           val fetchedCurrentWeather = apiWeather
               .getCurrentWeather(location, languageCode)
               .await()

           _downloadedCurrentWeather.postValue(fetchedCurrentWeather)
       }catch (e: NoConnectivityException){
           Log.e("Connectivity", "no internet connection")
       }
    }
}