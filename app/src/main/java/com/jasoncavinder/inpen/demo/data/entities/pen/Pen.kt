package com.jasoncavinder.inpen.demo.data.entities.pen

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
    @ColumnInfo(name = "pen_id") val penID: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "name") val name: String? = "My Insulin Pen",
    @ColumnInfo(name = "type") val type: String? = "Smart Pen",
    @ColumnInfo(name = "version") val version: String? = "Version 1.0"
)