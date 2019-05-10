/*
 * Copyright (c) 2019, Jason Cavinder <jason.cavinder@gmail.com>.
 * This project is licenced to the client of Upwork contract #21949291. It is not
 * licensed for public use. See the LICENSE.md file for details
 */

package com.jasoncavinder.insulinpendemoapp.database.entities.user

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.jasoncavinder.insulinpendemoapp.database.entities.BaseDao

@Dao
interface UserDao : BaseDao<User> {

    @Query("SELECT COUNT (id) FROM users")
    fun countUsers(): LiveData<Int>

    @Query("SELECT * FROM users")
    fun getAllUsers(): LiveData<List<User>>

    @Query("SELECT * FROM users WHERE id = :userId")
    fun getUser(userId: String): User

    @Query("SELECT id FROM users WHERE rowId = :rowId")
    fun getUserIdByRowId(rowId: Long): String

    @Query("SELECT id FROM users WHERE email = :email AND password = :password")
    fun getIdWithCredentials(email: String, password: String): String?

    @Query("UPDATE users SET providerId = :providerId WHERE id = :userId")
    fun changeProvider(userId: String, providerId: String): Int

    @Transaction
    @Query("SELECT * FROM users WHERE id = :userId")
    fun getUserProfile(userId: String): UserProfile

    @Transaction
    fun createUser(newUser: User): String {
        return getUserIdByRowId(insert(newUser))
    }

    @Query("DELETE FROM users")
    fun deleteAllUsers()

}
