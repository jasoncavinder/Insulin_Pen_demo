package com.jasoncavinder.old_inpen.demo.replaced.data.entities.message

import androidx.room.Dao
import androidx.room.Query
import com.jasoncavinder.insulinpendemoapp.database.entities.BaseDao
import com.jasoncavinder.insulinpendemoapp.database.entities.message.Message

@Dao
abstract class MessageDao :
    BaseDao<Message> {
    @Query("SELECT * FROM messages WHERE userID = :user_id")
    abstract fun getData(user_id: String): List<Message>
}


