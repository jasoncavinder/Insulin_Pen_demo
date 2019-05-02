package com.jasoncavinder.inpen.demo.data.entities

import androidx.room.*
import com.jasoncavinder.inpen.demo.data.entities.pen.Pen

@Dao
abstract class TransactionDao {

    @Transaction
    open fun addPenToUser(userID: String, pen: Pen) {
        insertPen(pen)
        updateUser(userID, pen.penID)
    } // TODO: make this more robust

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertPen(pen: Pen)

    @Query("UPDATE users SET penID = :penID WHERE user_id = :userID")
    abstract fun updateUser(userID: String, penID: String)
}


/*
  @Transaction
  void insert(Note... notes) {
    for (Note note : notes) {
      if (note instanceof Comment) {
        insert((Comment)note);
      }
      else if (note instanceof Link) {
        insert((Link)note);
      }
      else {
        throw new IllegalArgumentException("Um, wut dis? "+note.getClass().getCanonicalName());
      }
    }
  }
 */


//@Dao
//abstract class UserDao : BaseDao<User> {
//
//    @Query("SELECT COUNT (user_id) FROM users")
//    abstract fun countUsers(): LiveData<Int>
//
//    @Query("SELECT * FROM users WHERE user_id = :userID LIMIT 1")
//    abstract fun getUser(userID: String): LiveData<User>
//
//    @Query("SELECT * FROM users WHERE email = :email AND password = :password")
//    abstract fun login(email: String, password: String): User?
//
//    @Query("UPDATE users SET modified = :now WHERE user_id = :userID")
//    abstract fun logout(userID: String, now: Long = System.currentTimeMillis())
//
//    @Query("SELECT * FROM users WHERE rowid = :rowID")
//    abstract fun getUserByRowId(rowID: Long): User
//
//
//    @Transaction
//    open fun createUser(user: User): User? {
//        return this@UserDao.insert(user).run {
//            when (this) {
//                -1L -> null
//                else -> this@UserDao.getUserByRowId(this)
//            }
//        }
//    }
//
//    /**
//     * Delete all users.
//     */
//    @Query("DELETE FROM Users")
//    abstract fun deleteAllUsers()
//
//}
