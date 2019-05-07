//package com.jasoncavinder.inpen.demo.replaced.data.entities
//
//import androidx.room.*
//import com.jasoncavinder.insulinpendemoapp.database.entities.penPoints.Pen
//
//@Dao
//abstract class TransactionDao {
//
//    @Transaction
//    open fun addPenToUser(userID: String, penPoints: Pen) {
//        insertPen(penPoints)
//        updateUser(userID, penPoints.penID)
//    } // TODO: make this more robust
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    abstract fun insertPen(penPoints: Pen)
//
//    @Query("UPDATE users SET penID = :penID WHERE user_id = :userID")
//    abstract fun updateUser(userID: String, penID: String)
//}


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
//    abstract fun getLoggedInUser(userID: String): LiveData<User>
//
//    @Query("SELECT * FROM users WHERE email = :email AND password = :password")
//    abstract fun login(email: String, password: String): User?
//
//    @Query("UPDATE users SET modified = :now WHERE user_id = :userID")
//    abstract fun logout(userID: String, now: Long = System.currentTimeMillis())
//
//    @Query("SELECT * FROM users WHERE rowid = :rowID")
//    abstract fun getUserIdByRowId(rowID: Long): User
//
//
//    @Transaction
//    open fun createUser(loggedInUser: User): User? {
//        return this@UserDao.insert(loggedInUser).run {
//            when (this) {
//                -1L -> null
//                else -> this@UserDao.getUserIdByRowId(this)
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
