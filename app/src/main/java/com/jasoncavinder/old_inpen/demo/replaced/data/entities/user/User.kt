//package com.jasoncavinder.inpen.demo.replaced.data.entities.user
//
//import android.graphics.Bitmap
//import android.graphics.BitmapFactory
//import androidx.room.*
//import com.jasoncavinder.insulinpendemoapp.database.entities.provider.Provider
//import java.io.IOException
//import java.util.*
//
//
//@Entity(
//    tableName = "users",
//    indices = [
//        Index(value = ["email"], unique = true),
//        Index(value = ["id"], unique = true),
//        Index(value = ["providerID"])
//    ],
//    foreignKeys = [
//        ForeignKey(
//            entity = Provider::class,
//            parentColumns = arrayOf("id"),
//            childColumns = arrayOf("providerID")
//        )
//    ]
//)
//data class User(
//    @PrimaryKey
//    @ColumnInfo(name = "id") var userID: String = UUID.randomUUID().toString(),
//    var created: Calendar = Calendar.getInstance(),
//    var modified: Calendar = Calendar.getInstance(),
//    var email: String,
//    var password: String,
//    var firstName: String,
//    var lastName: String,
//    var providerID: String? = null
//) {
//    fun fullname(): String = "$firstName $lastName"
//    fun picture(): Bitmap? = try {
//        BitmapFactory.decodeFile(userID.replace("-","").toLowerCase().plus(".jpg"))
//    } catch (e: IOException) {
//        null
//    }
//}
