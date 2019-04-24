package com.jasoncavinder.inpen_demo.persistence

import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.*

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun dpTypeToInt(dpType: DataPointType?): String? {
        return dpType?.toString()
    }

    @TypeConverter
    fun stringToDPType(string: String?): DataPointType? {
        return string?.let { DataPointType.valueOf(it) }
    }

    @TypeConverter
    fun fromBitmap(image: Bitmap?): ByteArray? {
        val stream = ByteArrayOutputStream()
        image?.compress(CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    @TypeConverter
    fun toBitmap(image: ByteArray?): Bitmap? {
        return image?.let { BitmapFactory.decodeStream(ByteArrayInputStream(it)) }
    }
}