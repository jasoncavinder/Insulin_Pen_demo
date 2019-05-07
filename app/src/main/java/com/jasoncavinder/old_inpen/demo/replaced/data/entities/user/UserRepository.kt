//package com.jasoncavinder.inpen.demo.replaced.data.entities.loggedInUser
//
//import androidx.lifecycle.Transformations
//import com.jasoncavinder.inpen.demo.replaced.login.LoggedInUser
//import com.jasoncavinder.inpen.demo.z.data.model.LoggedInUser
//import com.jasoncavinder.insulinpendemoapp.utilities.HashUtils
//import com.jasoncavinder.inpen.demo.z.ui.ui.Result
//import java.io.IOException
//
//
//class AppRepository private constructor(private val _userDao: UserDao) {
//
//    var loggedInUser: LoggedInUser? = null
//        private set
//
//    val isLoggedIn: Boolean
//        get() = loggedInUser != null
//
//    init {
//        loggedInUser = null
//    }
//
//    fun localUsers() = _userDao.countUsers()
//
//    fun addUser(loggedInUser: User) = _userDao.insert(loggedInUser)
//
//    fun replaceUser(loggedInUser: User) = _userDao.insertReplace(loggedInUser)
//
//    fun getLoggedInUser(userID: String) = _userDao.getLoggedInUser(userID)
//
//    fun deleteAllUsers() = _userDao.deleteAllUsers()
//
//    fun updateUser(loggedInUser: User) = _userDao.update(loggedInUser)
//
//    fun logout() {
//        loggedInUser = null
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
//        if (result is Result.Success) this.loggedInUser = result.data
//        return result
//    }
//    val repositoryResult = Transformations.switchMap(userManager.loggedInUser) { loggedInUser ->
//        repository.getDataForUser(loggedInUser)
//    }
//
//
//    companion object {
//
//        @Volatile
//        private var _instance: AppRepository? = null
//
//        fun getInstance(userDao: UserDao) =
//            _instance ?: synchronized(this) {
//                _instance ?: AppRepository(userDao).also { _instance = it }
//            }
//    }
//}