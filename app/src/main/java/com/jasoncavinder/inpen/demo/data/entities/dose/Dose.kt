package com.jasoncavinder.inpen.demo.data.entities.dose

import androidx.room.*
import com.jasoncavinder.inpen.demo.data.entities.pen.Pen
import java.util.*

@Entity(
    tableName = "doses",
    foreignKeys = [
        ForeignKey(
            entity = Pen::class,
            parentColumns = arrayOf("pen_id"),
            childColumns = arrayOf("pen_id")
        )
    ],
    indices = [
        Index(value = ["dose_id"], unique = true),
        Index(value = ["pen_id"])
    ]
)
data class Dose(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "dose_id") val dose_id: Int,
    @ColumnInfo(name = "scheduled_ts") val scheduled_ts: Calendar?,
    @ColumnInfo(name = "scheduled_amount") val scheduled_amount: Float?,
    @ColumnInfo(name = "given_ts") val given_ts: Calendar?,
    @ColumnInfo(name = "given_amount") val given_amount: Float?,
    @ColumnInfo(name = "pen_id") val pen_id: String
)