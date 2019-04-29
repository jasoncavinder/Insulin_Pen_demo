package com.jasoncavinder.inpen.demo.data.entities.provider

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.*

@Entity(
    tableName = "providers",
    indices = [
        Index(value = ["provider_id"], unique = true)
    ]
)
data class Provider(
    @PrimaryKey
    @ColumnInfo(name = "provider_id") val provider_id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "position") val position: String?,
    @ColumnInfo(name = "rating") val rating: Float?,
    @ColumnInfo(name = "photo") val photo: Bitmap?,
    @ColumnInfo(name = "intro") val intro: String?,
    @ColumnInfo(name = "copay") val copay: Float?,
    @ColumnInfo(name = "languages") val languages: String?,
    @ColumnInfo(name = "education") val education: String?
)