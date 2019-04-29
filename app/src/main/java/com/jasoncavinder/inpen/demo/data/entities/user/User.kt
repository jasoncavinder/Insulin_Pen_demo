package com.jasoncavinder.inpen.demo.data.entities.user

import android.graphics.Bitmap
import androidx.room.*
import com.jasoncavinder.inpen.demo.data.entities.provider.Provider
import java.util.*


@Entity(
    tableName = "users",
    indices = [
        Index(value = ["email"], unique = true),
        Index(value = ["user_id"], unique = true),
        Index(value = ["provider_id"])
    ],
    foreignKeys = [
        ForeignKey(
            entity = Provider::class,
            parentColumns = arrayOf("provider_id"),
            childColumns = arrayOf("provider_id")
        )
    ]
)
data class User(
    @PrimaryKey
    @ColumnInfo(name = "user_id") var userID: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "created") var created: Calendar = Calendar.getInstance(),
    @ColumnInfo(name = "modified") var modified: Calendar = Calendar.getInstance(),
    @ColumnInfo(name = "email") var email: String,
    @ColumnInfo(name = "password") var password: String,
    @ColumnInfo(name = "first_name") var firstName: String,
    @ColumnInfo(name = "last_name") var lastName: String,
    @ColumnInfo(name = "avatar") var avatar: Bitmap? = null,
    @ColumnInfo(name = "picture") var picture: Bitmap? = null,
    @ColumnInfo(name = "provider_id") var providerID: String? = null,
    @ColumnInfo(name = "pen_id") var penID: String? = null
)
