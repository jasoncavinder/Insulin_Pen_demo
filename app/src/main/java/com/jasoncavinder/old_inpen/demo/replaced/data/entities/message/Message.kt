package com.jasoncavinder.old_inpen.demo.replaced.data.entities.message

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.jasoncavinder.insulinpendemoapp.database.entities.provider.Provider
import com.jasoncavinder.insulinpendemoapp.database.entities.user.User

@Entity(
    tableName = "messages",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("userID")
        ),
        ForeignKey(
            entity = Provider::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("providerID")
        )
    ],
    indices = [
        Index(value = ["messageID"], unique = true),
        Index(value = ["userID", "providerID"]),
        Index(value = ["providerID"])
    ]
)
data class Message(
    @PrimaryKey(autoGenerate = true)
    val messageID: Int,
    val timeStamp: Long,
    val userID: String,
    val providerID: String,
    val sent: Boolean, // true = from provider, false = to provider
    val content: String
) {
    var from: String = if (sent) userID else providerID
    var to: String = if (sent) providerID else userID
}