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
            parentColumns = arrayOf("provider_id"),
            childColumns = arrayOf("provider_id")
        )
    ],
    indices = [
        Index(value = ["message_id"], unique = true),
        Index(value = ["user_id", "provider_id"]),
        Index(value = ["provider_id"])
    ]
)
data class Message(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "message_id") val messageID: Int,
    @ColumnInfo(name = "timestamp") val timeStamp: Long,
    @ColumnInfo(name = "user_id") val userID: String,
    @ColumnInfo(name = "provider_id") val providerID: String,
    @ColumnInfo(name = "from") val from: Boolean, // true = from provider, false = to provider
    @ColumnInfo(name = "content") val content: String
)