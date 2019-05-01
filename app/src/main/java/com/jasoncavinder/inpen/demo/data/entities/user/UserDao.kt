package com.jasoncavinder.inpen.demo.data.entities.user

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.jasoncavinder.inpen.demo.data.entities.BaseDao

@Dao
abstract class UserDao : BaseDao<User> {

    @Query("SELECT COUNT (user_id) FROM users")
    abstract fun countUsers(): LiveData<Int>

    @Query("SELECT * FROM users WHERE user_id = :userID LIMIT 1")
    abstract fun getUser(userID: String): LiveData<User>

    @Query("SELECT * FROM users WHERE email = :email AND password = :password")
    abstract fun login(email: String, password: String): LiveData<User>

    @Query("SELECT * FROM users")
    abstract fun login(): LiveData<List<User>>

    @Query("SELECT * FROM users WHERE email = :email")
    abstract fun login(email: String): LiveData<List<User>>

    @Query("UPDATE users SET modified = :now WHERE user_id = :userID")
    abstract fun logout(userID: String, now: Long = System.currentTimeMillis())

    @Query("SELECT * FROM users WHERE rowid = :rowID")
    abstract fun getUserByRowId(rowID: Long): User


    @Transaction
    open fun createUser(user: User): User? {
        return this@UserDao.insert(user).run {
            when (this) {
                -1L -> null
                else -> this@UserDao.getUserByRowId(this)
            }
        }
    }

    /**
     * Delete all users.
     */
    @Query("DELETE FROM Users")
    abstract fun deleteAllUsers()

}
