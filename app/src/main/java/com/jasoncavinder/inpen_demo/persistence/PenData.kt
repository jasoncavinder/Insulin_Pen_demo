package com.jasoncavinder.inpen_demo.persistence

import androidx.room.*
import java.sql.Timestamp

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
data class PenData(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "data_point_id") val data_point_id: Int,
    @ColumnInfo(name = "pen_id") val pen_id: String,
    @ColumnInfo(name = "dp_ts") val ts: Timestamp,
    @ColumnInfo(name = "dp_type") val dp_type: DataPointType,
    @ColumnInfo(name = "data") val temp: Float?,     // may be temp, dose amount, etc
    @ColumnInfo(name = "time") val target_ts: Timestamp?,
    @ColumnInfo(name = "dose_id") val dose_id: Int?,
    @ColumnInfo(name = "elapsed") val elapsed: Long?
)