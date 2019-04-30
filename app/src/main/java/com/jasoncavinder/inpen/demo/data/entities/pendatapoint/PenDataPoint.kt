package com.jasoncavinder.inpen.demo.data.entities.pendatapoint

import androidx.room.*
import com.jasoncavinder.inpen.demo.data.entities.dose.Dose
import com.jasoncavinder.inpen.demo.data.entities.pen.Pen
import java.util.*

@Entity(
    tableName = "pen_data_points",
    foreignKeys = [
        ForeignKey(
            entity = Pen::class,
            parentColumns = arrayOf("pen_id"),
            childColumns = arrayOf("pen_id")
        ),
        ForeignKey(
            entity = Dose::class,
            parentColumns = arrayOf("dose_id"),
            childColumns = arrayOf("dose_id")
        )
    ],
    indices = [
        Index(value = ["data_point_id"], unique = true),
        Index(value = ["pen_id"]),
        Index(value = ["dose_id"])
    ]
)
data class PenDataPoint(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "data_point_id") val dataPointID: Int,
    @ColumnInfo(name = "pen_id") val penID: String,
    @ColumnInfo(name = "dp_ts") val dpTime: Calendar,
    @ColumnInfo(name = "dp_type") val dpType: DataPointType,
    @ColumnInfo(name = "data") val temp: Float?,     // may be temp, dose amount, etc
    @ColumnInfo(name = "time") val targetTime: Calendar?,
    @ColumnInfo(name = "dose_id") val doseID: Int?,
    @ColumnInfo(name = "elapsed") val elapsed: Long?
)