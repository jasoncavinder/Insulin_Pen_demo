//package com.jasoncavinder.inpen.demo.replaced.data.entities.pen
//
//import androidx.room.*
//import com.jasoncavinder.insulinpendemoapp.database.entities.user.User
//import java.util.*
//
//@Entity(
//    tableName = "pens",
//    indices = [
//        Index(value = ["id"], unique = true),
//        Index(value = ["userID"], unique = false)
//    ],
//    foreignKeys = [
//        ForeignKey(
//            entity = User::class,
//            parentColumns = ["id"],
//            childColumns = ["userID"]
//        )
//    ]
//
//)
//data class Pen(
//    @PrimaryKey
//    @ColumnInfo(name = "id") val penID: String = UUID.randomUUID().toString(),
//    val userID: String,
//    val name: String? = "My Insulin Pen",
//    val type: String? = "Smart Pen",
//    val version: String? = "Version 1.0"
//)