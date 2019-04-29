package com.jasoncavinder.inpen.demo.data.entities.message

import androidx.room.*
import com.jasoncavinder.inpen.demo.data.entities.provider.Provider
import com.jasoncavinder.inpen.demo.data.entities.user.User

@Entity(
    tableName = "messages",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = arrayOf("user_id"),
            childColumns = arrayOf("user_id")
        ),
        ForeignKey(
            entity = Provider::class,
            parentColumns = arrayOf("providerID"),
            childColumns = arrayOf("providerID")
        )
    ],
    indices = [
        Index(value = ["message_id"], unique = true),
        Index(value = ["user_id", "providerID"]),
        Index(value = ["providerID"])
    ]
)
data class Message(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "message_id") val messageid: Int,
    @ColumnInfo(name = "timestamp") val timeStamp: Long,
    @ColumnInfo(name = "user_id") val userid: String,
    @ColumnInfo(name = "providerID") val provider: String,
    @ColumnInfo(name = "from") val from: Boolean, // true = from provider, false = to provider
    @ColumnInfo(name = "content") val content: String
)