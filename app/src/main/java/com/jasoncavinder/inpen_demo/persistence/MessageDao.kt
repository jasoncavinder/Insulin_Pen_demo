package com.jasoncavinder.inpen_demo.persistence

import androidx.room.Dao
import androidx.room.Query

@Dao
abstract class MessageDao : BaseDao<Message> {
    @Query("SELECT * FROM messages")
    abstract fun getData(): List<Message>
}


