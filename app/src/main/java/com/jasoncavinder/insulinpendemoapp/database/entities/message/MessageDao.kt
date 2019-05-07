package com.jasoncavinder.insulinpendemoapp.database.entities.message

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.jasoncavinder.insulinpendemoapp.database.entities.BaseDao

@Dao
interface MessageDao : BaseDao<Message> {

    @Query("SELECT * FROM messages WHERE id = :messageId")
    fun getMessage(messageId: Int): LiveData<Message>

    @Query("SELECT * FROM messages")
    fun getAllMessages(): LiveData<List<Message>>

    @Query("SELECT * FROM messages WHERE userId = :userId")
    fun getUserMessages(userId: String): LiveData<List<Message>>

    @Query("SELECT * FROM messages WHERE userId = :userId and providerID = :providerId")
    fun getConversation(userId: String, providerId: String): LiveData<List<Message>>

}

