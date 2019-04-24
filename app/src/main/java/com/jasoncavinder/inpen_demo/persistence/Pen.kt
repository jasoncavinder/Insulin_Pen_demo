package com.jasoncavinder.inpen_demo.persistence

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.*

@Entity(
    tableName = "pens",
    indices = [
        Index(value = ["pen_id"], unique = true)
    ]
)
data class Pen(
    @PrimaryKey
    @ColumnInfo(name = "pen_id") val pen_id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "type") val type: String,
    @ColumnInfo(name = "version") val version: String
)