package com.jasoncavinder.inpen_demo.persistence

import androidx.lifecycle.LiveData
import androidx.room.*
import java.util.*

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addUser(user: User): Long

    @Query("SELECT COUNT (user_id) FROM users")
    fun countUsers(): LiveData<Int>

    @Query("SELECT * FROM users WHERE user_id = :id")
    fun getUserById(id: String): Optional<User>

    @Query("SELECT user_id FROM users WHERE email = :email AND password = :password LIMIT 1")
    fun getUserByLogin(email: String, password: String): String

    @Update
    fun updateUser(user: User)

    @Delete
    fun deleteUser(user: User)

}
