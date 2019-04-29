//package com.jasoncavinder.inpen.demo.data.entities.user
//
//import androidx.lifecycle.Transformations
//import com.jasoncavinder.inpen.demo.login.LoggedInUser
//import com.jasoncavinder.inpen_demo.z.data.model.LoggedInUser
//import com.jasoncavinder.inpen.demo.utilities.HashUtils
//import com.jasoncavinder.inpen_demo.z.ui.ui.Result
//import java.io.IOException
//
//
//class UserRepository private constructor(private val _userDao: UserDao) {
//
//    var user: LoggedInUser? = null
//        private set
//
//    val isLoggedIn: Boolean
//        get() = user != null
//
//    init {
//        user = null
//    }
//
//    fun localUsers() = _userDao.countUsers()
//
//    fun addUser(user: User) = _userDao.insert(user)
//
//    fun replaceUser(user: User) = _userDao.insertReplace(user)
//
//    fun getUser(userID: String) = _userDao.getUser(userID)
//
//    fun deleteAllUsers() = _userDao.deleteAllUsers()
//
//    fun updateUser(user: User) = _userDao.update(user)
//
//    fun logout() {
//        user = null
////        _userDao.logout()
//    }
//
//    fun ui(e: String, p: String): Result<LoggedInUser> {
//        val result = _userDao.login(e, HashUtils.sha512(p)).value
//            .run {
//                when (this) {
//                    null -> Result.Error(IOException("Invalid email or password.\n Please try again."))
//                    else -> Result.Success(
//                        LoggedInUser(
//                            userID, email, firstName, lastName, avatar, picture, inpenID, providerID
//                        )
//                    )
//                }
//            }
//        if (result is Result.Success) this.user = result.data
//        return result
//    }
//    val repositoryResult = Transformations.switchMap(userManager.user) { user ->
//        repository.getDataForUser(user)
//    }
//
//
//    companion object {
//
//        @Volatile
//        private var _instance: UserRepository? = null
//
//        fun getInstance(userDao: UserDao) =
//            _instance ?: synchronized(this) {
//                _instance ?: UserRepository(userDao).also { _instance = it }
//            }
//    }
//}