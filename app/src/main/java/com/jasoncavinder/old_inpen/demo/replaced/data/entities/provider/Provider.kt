//package com.jasoncavinder.inpen.demo.replaced.data.entities.provider
//
//import android.graphics.Bitmap
//import android.graphics.BitmapFactory
//import androidx.room.ColumnInfo
//import androidx.room.Entity
//import androidx.room.Index
//import androidx.room.PrimaryKey
//import java.io.IOException
//import java.util.*
//
//@Entity(
//    tableName = "providers",
//    indices = [
//        Index(value = ["id"], unique = true)
//    ]
//)
//data class Provider(
//    @PrimaryKey
//    @ColumnInfo(name = "id")
//    val providerID: String = UUID.randomUUID().toString(),
//    val name: String,
//    val position: String?,
//    val rating: Float = 0f,
//    val intro: String?,
//    val copay: Float?,
//    val languages: List<String>? = null,
//    val education: String?
//) {
//    fun picture(): Bitmap? = try {
//        BitmapFactory.decodeFile(providerID.replace("-","").toLowerCase().plus(".jpg"))
//    } catch (e: IOException) {
//        null
//    }
//}