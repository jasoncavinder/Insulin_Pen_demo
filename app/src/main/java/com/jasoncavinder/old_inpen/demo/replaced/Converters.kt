package com.jasoncavinder.old_inpen.demo.replaced

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
    fun dpTypeToInt(dpType: DataPointType?): String? {
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

/*    @TypeConverter
    fun bitmapToBlob(bitmap: Bitmap?): Blob? {
        return bitmap?.run {
            val outputStream = ByteArrayOutputStream()
            compress(CompressFormat.PNG, 100, outputStream)
            Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
        } as Blob
    }

    @TypeConverter
    fun stringToBlob(string: String?): Blob? {
        return string as Blob
    }

    @TypeConverter
    fun blobToBitmap(base64Str: Blob?): Bitmap? {
        return (base64Str as String?)?.run {
            Base64.decode(substring(indexOf(",") + 1), Base64.DEFAULT)
        }?.run {
            BitmapFactory.decodeByteArray(this, 0, size)
        }
    }*/
}