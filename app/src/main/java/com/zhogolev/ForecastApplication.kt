package com.zhogolev

import android.app.Application
import android.content.Context
import android.preference.PreferenceManager
import com.google.android.gms.location.LocationServices
import com.jakewharton.threetenabp.AndroidThreeTen
import com.zhogolev.data.db.ForecastDatabase
import com.zhogolev.data.network.*
import com.zhogolev.data.provider.LocationProvider
import com.zhogolev.data.provider.LocationProviderImpl
import com.zhogolev.data.provider.UnitProvider
import com.zhogolev.data.provider.UnitProviderImpl
import com.zhogolev.data.repository.ForecastRepository
import com.zhogolev.data.repository.ForecastRepositoryImpl
import com.zhogolev.ui.weather.current.CurrentWeatherViewModelFactory
import com.zhogolev.ui.weather.future.detail.FutureDetailModelFactory
import com.zhogolev.ui.weather.future.list.FutureListViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.*
import org.threeten.bp.LocalDate

class ForecastApplication: Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@ForecastApplication))

        bind() from singleton { ForecastDatabase(instance()) }
        bind() from singleton { instance<ForecastDatabase>().currentWeatherDao() }
        bind() from singleton { instance<ForecastDatabase>().futureWeatherDao() }
        bind() from singleton { instance<ForecastDatabase>().weatherLocationDao() }
        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(instance())}
        bind() from singleton {ApiWeather(instance())}
        bind<WeatherNetworkDataSource>() with singleton { WeatherNetworkDataSourceImpl(instance())}
        bind() from provider { LocationServices.getFusedLocationProviderClient(instance<Context>())}
        bind<LocationProvider>() with singleton { LocationProviderImpl(instance(),instance()) }
        bind<ForecastRepository>() with singleton { ForecastRepositoryImpl(instance(),instance(), instance(), instance(), instance()) }
        bind<UnitProvider>() with singleton { UnitProviderImpl(instance()) }
        bind() from provider { CurrentWeatherViewModelFactory(instance(), instance()) }
        bind() from provider { FutureListViewModelFactory(instance(), instance()) }
        bind() from factory {detailDate: LocalDate -> FutureDetailModelFactory(detailDate, instance(),instance())}
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        PreferenceManager.setDefaultValues(this, R.xml.prefrences, true)
    }
}