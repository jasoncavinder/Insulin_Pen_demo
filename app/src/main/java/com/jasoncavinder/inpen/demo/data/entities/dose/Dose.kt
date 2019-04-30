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
    @ColumnInfo(name = "dose_id") val doseID: Int,
    @ColumnInfo(name = "scheduled_ts") val scheduledTime: Calendar?,
    @ColumnInfo(name = "scheduled_amount") val scheduledAmount: Float?,
    @ColumnInfo(name = "given_ts") val givenTime: Calendar?,
    @ColumnInfo(name = "given_amount") val givenAmount: Float?,
    @ColumnInfo(name = "user_id") val userID: String,
    @ColumnInfo(name = "pen_id") val penID: String
)