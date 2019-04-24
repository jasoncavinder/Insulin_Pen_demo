package com.jasoncavinder.inpen_demo.persistence

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.samclarke.android.util.HashUtils
import java.util.*

class UserRepository(application: Application) {

    enum class Existence { YES, NO, MAYBE }

    private val userDao: UserDao
    private val localUsers: LiveData<Int>
    private val user: MutableLiveData<Optional<User>>
    private val userExists: MutableLiveData<Existence>
    private var userID: String?
    val YES = Existence.YES
    val NO = Existence.NO
    val MAYBE = Existence.MAYBE

    init {
        val db = InPenAppDatabase.getDatabase(application)!!
        userDao = db.userDao()
        localUsers = userDao.countUsers()
        userID = null
        user = MutableLiveData(Optional.empty())
        userExists = MutableLiveData(MAYBE)
    }

    private fun hashPassword(password: String): String {
        return HashUtils.sha512(password)
        /*
        val random = SecureRandom()
        val salt = byteArrayOf(16)
        random.nextBytes(salt)
        val spec = PBEKeySpec(password.toCharArray(), salt, 65536,128)
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
        val hash = factory.generateSecret(spec).encoded

        return hash.toString()
        */
    }

    fun getUserCount(): LiveData<Int> {
        return localUsers
    }

    fun getCurrentUser(): LiveData<Optional<User>> {
        return user
    }

    fun doesUserExist(): LiveData<Existence> {
        return userExists
    }

    fun validateUser(email: String, password: String) {
        Log.d("Validate User", "Entered function")
        userExists.postValue(MAYBE)
        Log.d("Validate User", "userExists = ${userExists.value.toString()}")
        userID = null
        Log.d("Validate User", "starting thread")
        Thread {
            Log.d("Validate User", "running getUserByLogin($email, ${hashPassword(password)})")
            userID = (userDao.getUserByLogin(email, hashPassword(password)))
            Log.d("Validate User", "received userID = $userID")
            when (userID?.length ?: 0) {
                0 -> {
                    Log.d("Validate User", "setting userExists to NO")
                    userExists.postValue(NO)
                }
                else -> {
                    Log.d("Validate User", "running getUserById($userID!!)")
                    user.postValue(userDao.getUserById(userID!!))
                    Log.d("Validate User", "received $user")
                    Log.d("Validate User", "setting userExists to YES")
                    userExists.postValue(YES)
                }
            }
            Log.d("Validate User", "thread complete")
        }.start()
    }

    fun createUser(email: String, password: String, firstName: String, lastName: String) {
        Log.d("Create User", "Entered function")
        userExists.postValue(NO)
        Log.d("Create User", "starting thread")
        Thread {
            try {
                Log.d("Create User", "running addUser with ($email, ${hashPassword(password)}, $firstName, $lastName)")
                val result = userDao.addUser(
                    User(
                        email = email,
                        password = hashPassword(password),
                        firstName = firstName,
                        lastName = lastName
                    )
                )
                Log.d("Create User", "addUser returned $result.")
                Log.d("Create User", "running validateUser with ($email, $password)")
                validateUser(email, password)
            } catch (e: Error) {
                Log.d("Create User", "addUser erred: $e")
            }
            Log.d("Create User", "thread complete")
        }.start()
    }

    companion object {

        private var instance: UserRepository? = null

        fun getInstance(application: Application): UserRepository {
            if (instance == null) {
                synchronized(UserRepository::class.java) {
                    if (instance == null) {
                        instance = UserRepository(application)
                    }
                }
            }
            return instance!!
        }
    }

}

