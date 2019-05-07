//package com.jasoncavinder.inpen.demo.replaced.data.entities.user
//
//import androidx.lifecycle.LiveData
//import androidx.room.Dao
//import androidx.room.Query
//import androidx.room.Transaction
//import com.jasoncavinder.insulinpendemoapp.database.entities.BaseDao
//import com.jasoncavinder.inpen.demo.replaced.utilities.DEFAULT_USER_TIMEOUT_MINUTES
//import com.jasoncavinder.insulinpendemoapp.database.entities.user.User
//import java.util.*
//
//@Dao
//abstract class UserDao : BaseDao<User> {
//
//    @Query("SELECT COUNT (id) FROM users")
//    abstract fun countUsers(): LiveData<Int>
//
//    @Query("SELECT * FROM users WHERE id = :userID LIMIT 1")
//    protected abstract fun getUser(userID: String): User
//
//    @Query("SELECT id FROM users WHERE email = :email AND password = :password")
//    abstract fun getIdWithCredentials(email: String, password: String): String?
//
//    @Query("UPDATE users SET modified = :now WHERE id = :userID")
//    protected abstract fun touch(userID: String, now: Long = System.currentTimeMillis())
//
//    @Query("SELECT modified FROM users WHERE id = :userID")
//    protected abstract fun lastTime(userID: String): Calendar
//
//    @Query("UPDATE users SET providerID = :providerID WHERE id = :userID")
//    abstract fun updateProvider(userID: String, providerID: String): Int  // number of updated rows
//
//    fun loggedIn(userID: String): Boolean =
//        (lastTime(userID)
//            .apply { add(Calendar.MINUTE, DEFAULT_USER_TIMEOUT_MINUTES) } <
//                Calendar.getInstance()
//                ).also { if (it) touch(userID) }
//
//    @Transaction
//    @Synchronized
//    open fun login(email: String, password: String): String? {
//        return getIdWithCredentials(email, password)?.also { touch(it) }
//    }
//
//    @Transaction
//    @Synchronized
//    open fun getUserById(userID: String): User? {
//        return when (loggedIn(userID)) {
//            false -> null
//            true -> getUser(userID).also { touch(userID) }
//        }
//    }
//
//
///*
//    @Query("SELECT * FROM users WHERE rowid = :rowID")
//    abstract fun getUserIdByRowId(rowID: Long): User
//*/
//
//
////    }
//
//    /**
//     * Delete all users.
//     */
//    @Query("DELETE FROM Users")
//    abstract fun deleteAllUsers()
//
//}
//
////    @Transaction
////    open fun createUser(user: User): Int {
////        return this@UserDao.insert(user)//.run {
////            when (this) {
////                -1L -> null
////                else -> this@UserDao.getUserIdByRowId(this)
////            }
//
////        }
