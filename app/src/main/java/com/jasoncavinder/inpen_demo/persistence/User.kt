package com.jasoncavinder.inpen_demo.persistence

import android.graphics.Bitmap
import androidx.room.*
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
    @ColumnInfo(name = "user_id") val user_id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "email") val email: String,
    @ColumnInfo(name = "password") val password: String,
    @ColumnInfo(name = "firstName") val firstName: String,
    @ColumnInfo(name = "lastName") val lastName: String,
    @ColumnInfo(name = "avatar") val avatar: Bitmap? = null,
    @ColumnInfo(name = "picture") val picture: Bitmap? = null,
    @ColumnInfo(name = "inpen_id") val inpen_id: String? = null,
    @ColumnInfo(name = "provider_id") val provider_id: String? = null
)
