package com.zhogolev.ui.weather.future.list

import android.annotation.SuppressLint
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import com.zhogolev.R
import com.zhogolev.data.db.unitalized.future.MetricSimpleFutureWeatherEntry
import com.zhogolev.data.db.unitalized.future.UnitSpecificSimpleFutureWeatherEntry
import kotlinx.android.synthetic.main.item_future_weather.*
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle
import com.zhogolev.internal.glide.GlideApp

class FutureWeatherItem(
    private val weatherEntry: UnitSpecificSimpleFutureWeatherEntry
) : Item(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.apply {
            textView_condition.text = weatherEntry.conditionText
            updateDate()
            updateTemperature()
            updateConditionImage()
        }
    }

    override fun getLayout() = R.layout.item_future_weather

    private fun ViewHolder.updateDate(){
        val dtFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
        textView_date.text = weatherEntry.date.format(dtFormatter)
    }

    @SuppressLint("SetTextI18n")
    private fun ViewHolder.updateTemperature(){
        val unitAbbreviation = if (weatherEntry is MetricSimpleFutureWeatherEntry) "@C" else "@F"
        textView_temperature.text = "${weatherEntry.avgTemperature}$unitAbbreviation"
    }

    private fun ViewHolder.updateConditionImage(){
        GlideApp.with(this.containerView)
            .load("http:${weatherEntry.conditionIconUrl}")
            .into(imageView_condition_icon)
    }
}