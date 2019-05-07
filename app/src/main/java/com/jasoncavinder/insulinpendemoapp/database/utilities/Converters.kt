/*
 * Copyright (c) 2019, Jason Cavinder <jason.cavinder@gmail.com>.
 * This project is licenced to the client of Upwork contract #21949291. It is not
 * licensed for public use. See the LICENSE.md file for details
 */

package com.jasoncavinder.insulinpendemoapp.database.utilities

import androidx.room.TypeConverter
import com.jasoncavinder.insulinpendemoapp.database.entities.pendatapoint.DataPointType
import java.util.*

class Converters {
    @TypeConverter
    fun calendarToDatestamp(calendar: Calendar): Long = calendar.timeInMillis

    @TypeConverter
    fun datestampToCalendar(value: Long): Calendar =
        Calendar.getInstance().apply { timeInMillis = value }

    @TypeConverter
    fun dpTypeToString(dpType: DataPointType?): String? {
        return dpType?.toString()
    }

    @TypeConverter
    fun stringToDPType(string: String?): DataPointType? {
        return string?.let { DataPointType.valueOf(it) }
    }

    @TypeConverter
    fun listToString(list: List<String>): String {
        return list.joinToString("__")
    }

    @TypeConverter
    fun stringToList(string: String): List<String> {
        return string.split("__")
    }
}