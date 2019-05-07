/*
 * Copyright (c) 2019, Jason Cavinder <jason.cavinder@gmail.com>.
 * This project is licenced to the client of Upwork contract #21949291. It is not
 * licensed for public use. See the LICENSE.md file for details
 */

package com.jasoncavinder.insulinpendemoapp.database.entities.pendatapoint

import androidx.room.*
import com.jasoncavinder.insulinpendemoapp.database.entities.pen.Pen
import java.util.*

@Entity(
    tableName = "pen_data_points",
    foreignKeys = [
        ForeignKey(entity = Pen::class, parentColumns = ["id"], childColumns = ["penId"])
    ],
    indices = [
        Index(value = ["id"], unique = true),
        Index(value = ["penId"])
    ]
)
data class PenDataPoint(
    val dpTime: Calendar,
    val dpType: DataPointType,
    val data: Float? = null,     // may be temp, dose amount, etc
    val targetTime: Calendar? = null,
    val elapsed: Long? = null
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var dataPointId: Int = 0
    var penId: String = ""
}