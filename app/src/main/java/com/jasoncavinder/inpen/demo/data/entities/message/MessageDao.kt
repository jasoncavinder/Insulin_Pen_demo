package com.jasoncavinder.inpen.demo.data.entities.message

import androidx.room.Dao
import androidx.room.Query
import com.jasoncavinder.inpen.demo.data.entities.BaseDao

@Dao
abstract class MessageDao :
    BaseDao<Message> {
    @Query("SELECT * FROM messages")
    abstract fun getData(): List<Message>
}


