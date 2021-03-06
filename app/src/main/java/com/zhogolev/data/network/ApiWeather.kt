package com.zhogolev.data.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.zhogolev.data.network.response.CurrentWeatherResponse
import com.zhogolev.data.network.response.FutureWeatherResponse
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val API_KEY = "976b883815564e30ace51643190805"
const val API_URL = "https://api.apixu.com/v1/"

interface ApiWeather {

    @GET("current.json")
    fun getCurrentWeather(
        @Query("q") location: String,
        @Query("lang") languageCode: String = "en"
    ): Deferred<CurrentWeatherResponse>

    @GET("forecast.json")
    fun getFutureWeather(
        @Query("q") location: String,
        @Query("lang") languageCode: String = "en",
        @Query("days") days: Int
    ): Deferred<FutureWeatherResponse>

    companion object {
        operator fun invoke(
            connectivityInterceptor: ConnectivityInterceptor
        ): ApiWeather {
            val requestIntercepter = Interceptor { chain ->
                val url = chain.request()
                    .url()
                    .newBuilder()
                    .addQueryParameter("key", API_KEY)
                    .build()
                val request = chain.request()
                    .newBuilder()
                    .url(url)
                    .build()

                return@Interceptor chain.proceed(request)
            }
            val httpClient = OkHttpClient.Builder()
                .addInterceptor(requestIntercepter)
                .addInterceptor(connectivityInterceptor)
                .build()

            return Retrofit.Builder()
                .client(httpClient)
                .baseUrl(API_URL)
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiWeather::class.java)
        }
    }
}