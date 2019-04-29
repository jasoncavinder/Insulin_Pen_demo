package com.jasoncavinder.inpen.demo.data

import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.room.TypeConverter
import com.jasoncavinder.inpen.demo.data.entities.pendata.DataPointType
import java.io.ByteArrayOutputStream
import java.util.*

class Converters {
    //    @TypeConverter
//    fun fromOptional(value: Optional<Any>) {
//        // TODO:
//    }
//
//    @TypeConverter
//    fun toOptional(value: LiveData<User>): Optional<LiveData<User>>{
//        return Optional.of(value)
//    }
    @TypeConverter
    fun calendarToDatestamp(calendar: Calendar): Long = calendar.timeInMillis

    @TypeConverter
    fun datestampToCalendar(value: Long): Calendar =
        Calendar.getInstance().apply { timeInMillis = value }

    @TypeConverter
    fun dpTypeToInt(dpType: DataPointType?): String? {
        return dpType?.toString()
    }

    @TypeConverter
    fun stringToDPType(string: String?): DataPointType? {
        return string?.let { DataPointType.valueOf(it) }
    }

    @TypeConverter
    fun bitmapToString(bitmap: Bitmap?): String? {
        return bitmap?.run {
            val outputStream = ByteArrayOutputStream()
            compress(CompressFormat.PNG, 100, outputStream)
            Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
        }
    }

    @TypeConverter
    fun stringToBitmap(base64Str: String?): Bitmap? {
        return base64Str?.run {
            Base64.decode(substring(indexOf(",") + 1), Base64.DEFAULT)
        }?.run {
            BitmapFactory.decodeByteArray(this, 0, size)
        }
    }
}