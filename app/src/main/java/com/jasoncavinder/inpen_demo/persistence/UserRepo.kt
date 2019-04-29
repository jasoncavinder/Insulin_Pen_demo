//package com.jasoncavinder.inpen_demo.persistence
//
//import android.app.Application
//import android.util.Log
//import androidx.lifecycle.LiveData
//import com.jasoncavinder.inpen.demo.data.AppDatabase
//import com.jasoncavinder.inpen_demo.z.data.users.User
//import com.jasoncavinder.inpen_demo.z.data.users.UserDao
//import com.jasoncavinder.inpen.demo.utilities.HashUtils
//
//class UserRepository() {
//
//    private val _db: AppDatabase = AppDatabase.getInstance(application)!!
//    private val _userDao: UserDao = _db.userDao()
//
//    var localUsers: LiveData<Int>
//        private set
//
//    var user: LiveData<User>? = null
//        private set
//
//    init {
//        localUsers = _userDao.countUsers()
//        user = null
//    }
//
//    private fun hashPassword(password: String): String {
//        return HashUtils.sha512(password)
//    }
//
//    fun getCurrentUser(): LiveData<User>? {
//        return user
//    }
//
//    fun doesUserExist(): LiveData<Existence> {
//        return userExists
//    }
//
//    fun validateUser(email: String, password: String) {
//        Log.d("Validate User", "Entered function")
//        userExists.postValue(MAYBE)
//        Log.d("Validate User", "userExists = ${userExists.value.toString()}")
//        user_id = null
//        Log.d("Validate User", "starting thread")
//        Thread {
//            Log.d("Validate User", "running getUserByLogin($email, ${hashPassword(password)})")
//            user_id = (_userDao.getUserByLogin(email, hashPassword(password)))
//            Log.d("Validate User", "received user_id = $user_id")
//            when (user_id?.length ?: 0) {
//                0 -> {
//                    Log.d("Validate User", "setting userExists to NO")
//                    userExists.postValue(NO)
//                }
//                else -> {
//                    Log.d("Validate User", "running getUserById($user_id!!)")
//                    user.postValue(_userDao.getUserById(user_id!!))
//                    Log.d("Validate User", "received $user")
//                    Log.d("Validate User", "setting userExists to YES")
//                    userExists.postValue(YES)
//                }
//            }
//            Log.d("Validate User", "thread complete")
//        }.start()
//    }
//
//    fun createUser(email: String, password: String, firstName: String, lastName: String) {
//        Log.d("Create User", "Entered function")
//        userExists.postValue(NO)
//        Log.d("Create User", "starting thread")
//        Thread {
//            try {
//                Log.d("Create User", "running addUser with ($email, ${hashPassword(password)}, $firstName, $lastName)")
//                val result = _userDao.addUser(
//                    User(
//                        email = email,
//                        password = hashPassword(password),
//                        firstName = firstName,
//                        lastName = lastName
//                    )
//                )
//                Log.d("Create User", "addUser returned $result.")
//                Log.d("Create User", "running validateUser with ($email, $password)")
//                validateUser(email, password)
//            } catch (e: Error) {
//                Log.d("Create User", "addUser erred: $e")
//            }
//            Log.d("Create User", "thread complete")
//        }.start()
//    }
//
//    companion object {
//
//        private var instance: UserRepository? = null
//
//        fun getInstance(application: Application): UserRepository {
//            if (instance == null) {
//                synchronized(UserRepository::class.java) {
//                    if (instance == null) {
//                        instance = UserRepository(application)
//                    }
//                }
//            }
//            return instance!!
//        }
//    }
//
//}
//
