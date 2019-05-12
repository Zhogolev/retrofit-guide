package com.zhogolev.data.db.unitalized.future.detail

import org.threeten.bp.LocalDate

interface UnitSpecificWeatherDetailEntry {
    val date: LocalDate
    val maxTemperature: Double
    val minTemperature: Double
    val avgTemperature: Double
    val conditionText: String
    val conditionURL: String
    val maxWindSpeed: Double
    val totalPrecepitation: Double
    val avgVisibilityDistance: Double
    val uv: Double
}