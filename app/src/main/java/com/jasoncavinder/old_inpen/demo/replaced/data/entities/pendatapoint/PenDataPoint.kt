//package com.jasoncavinder.inpen.demo.replaced.data.entities.pendatapoint
//
//import androidx.room.*
//import com.jasoncavinder.insulinpendemoapp.database.entities.pen.Pen
//import com.jasoncavinder.insulinpendemoapp.database.entities.pendatapoint.DataPointType
//import java.util.*
//
//@Entity(
//    tableName = "pen_data_points",
//    foreignKeys = [
//        ForeignKey(
//            entity = Pen::class,
//            parentColumns = arrayOf("id"),
//            childColumns = arrayOf("penID")
//        )    ],
//    indices = [
//        Index(value = ["id"], unique = true),
//        Index(value = ["penID"])
//    ]
//)
//data class PenDataPoint(
//    @PrimaryKey(autoGenerate = true)
//    @ColumnInfo(name = "id") val dataPointID: Int,
//    val penID: String,
//    val dpTime: Calendar,
//    val dpType: DataPointType,
//    val _data: Float? = null,     // may be temp, dose amount, etc
//    val targetTime: Calendar? = null,
//    val elapsed: Long? = null
//)