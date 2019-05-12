package com.zhogolev.data.db

import androidx.room.TypeConverter
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.LocalDate

object LocalDateConverter {
    @TypeConverter
    @JvmStatic
    fun stringToDate(str: String?) = str?.let {
        LocalDate.parse(it, DateTimeFormatter.ISO_LOCAL_DATE)
    }

    @TypeConverter
    @JvmStatic
    fun dateToString(date: LocalDate?) = date?.format(DateTimeFormatter.ISO_LOCAL_DATE)
}