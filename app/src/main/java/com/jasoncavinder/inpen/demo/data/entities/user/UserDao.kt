package com.jasoncavinder.inpen.demo.data.entities.user

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.jasoncavinder.inpen.demo.data.entities.BaseDao

@Dao
interface UserDao : BaseDao<User> {

    @Query("SELECT COUNT (user_id) FROM users")
    fun countUsers(): LiveData<Int>

    @Query("SELECT * FROM users WHERE user_id = :userID LIMIT 1")
    fun getUser(userID: String): LiveData<User>

    @Query("SELECT * FROM users WHERE email = :email AND password = :password LIMIT 1")
    fun login(email: String, password: String): LiveData<User>


//    fun logout() {
//        return
//         TODO:
//    }

    /**
     * Delete all users.
     */
    @Query("DELETE FROM Users")
    fun deleteAllUsers()

}
