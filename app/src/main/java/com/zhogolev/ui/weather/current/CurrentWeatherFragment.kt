package com.zhogolev.ui.weather.current

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer

import com.zhogolev.R
import com.zhogolev.internal.glide.GlideApp
import com.zhogolev.ui.base.ScopedFragment
import kotlinx.android.synthetic.main.current_weather_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class CurrentWeatherFragment : ScopedFragment(), KodeinAware {

    override val kodein by closestKodein()
    private val viewModelFactory: CurrentWeatherViewModelFactory by instance()
    private val TAG = "CURRENT_WEATHER"

    companion object {
        fun newInstance() = CurrentWeatherFragment()
    }

    private lateinit var viewModel: CurrentWeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.current_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //added view model factory
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CurrentWeatherViewModel::class.java)

        bindUI()
    }

    private fun bindUI() = launch {

        val currentWeather = viewModel.weather.await()

        val weatherLocation = viewModel.weatherLocation.await()

        weatherLocation.observe(this@CurrentWeatherFragment, Observer {location ->
            if(location == null) return@Observer

            updateLocation(location.name)

        })


        currentWeather.observe(this@CurrentWeatherFragment, Observer {
            Log.d(TAG, "observed value = $it")
            if (it == null)
                return@Observer

            group_loading.visibility = View.GONE
            //  updateLocation("Los Angeles")
            updateSubtitle()
            updateTemp(it.temperature, it.feelsLikeTemperature)
            updatePrecepitation(it.precipitationVolume)
            updateWind(it.windDirection, it.windSpeed)
            updateVisibility(it.visibilityDistance)

            GlideApp.with(this@CurrentWeatherFragment)
                .load("http:${it.conditionIconUrl}")
                .into(imageView_condition)
        })
    }

    private fun updateLocation(location: String) {
        (activity as? AppCompatActivity)?.supportActionBar?.title = location
    }

    private fun updateSubtitle() {
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = "Today"
    }

    private fun updateTemp(temp: Double, feelsTemp: Double) {
        val unitAbbriviation = getLocalaziedMetric("Ce", "Fh")
        textView_tempreture.text = "$temp$unitAbbriviation"
        textView_feels_like.text = "Feels like $feelsTemp$unitAbbriviation"
    }

    private fun updatePrecepitation(preceitation: Double) {
        val unitAbbriviation = getLocalaziedMetric("mm", "in")
        textView_precepitation.text = "Precepitation: $preceitation $unitAbbriviation"
    }

    private fun getLocalaziedMetric(metric: String, imperial: String): String {
        return if (viewModel.isMetricUnit) metric else imperial
    }

    private fun updateWind(windDirection: String, windSpeed: Double) {
        val unitAbbriviation = getLocalaziedMetric("kph", "mph")
        textView_wind.text = "Wind: $windDirection $windSpeed$unitAbbriviation"
    }


    private fun updateVisibility(distance: Double) {
        val unitAbbriviation = getLocalaziedMetric("km", "mi.")
        textView_visibility.text = "Visibility: $distance $unitAbbriviation"
    }
}


