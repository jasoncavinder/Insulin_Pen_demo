package com.jasoncavinder.inpen_demo.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.jasoncavinder.inpen_demo.persistence.UserRepository
import com.jasoncavinder.inpen_demo.persistence.UserRepository.Existence.*
import com.jasoncavinder.inpen_demo.ui.UserViewModel.AuthenticationState.*

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepository = UserRepository.getInstance(application)

    enum class AuthenticationState {
        UNAUTHENTICATED, REGISTERING, ATTEMPTING_AUTHENTICATION, INVALID_AUTHENTICATION, AUTHENTICATED
    }

    /*
    class Profile {
        private var authenticated = false
        private var firstName = ""
        private var lastName = ""

        fun isAuthenticated(): Boolean { return authenticated }
        fun getFirstName(): String { return firstName }
        fun getLastName(): String { return lastName}
    }
     */

    private val localUserCount = userRepository.getUserCount()
    private val currentUser = userRepository.getCurrentUser()
    private val userExists = userRepository.doesUserExist()
    val authenticationState = MutableLiveData(UNAUTHENTICATED)
    private val reason = MutableLiveData<String>("")

    val mediator = MediatorLiveData<Any>()

/*
    TODO: Use authentication tokens
    private var authToken = ""
*/

    init {
        mediator.addSource(currentUser) { }
        mediator.addSource(authenticationState) { }
        mediator.addSource(userExists) {
            when (it) {
                MAYBE -> authenticationState.value = ATTEMPTING_AUTHENTICATION
                YES -> authenticationState.value = AUTHENTICATED
                NO -> {
                    reason.value =
                        "That email/password combination was not found.\nPlease double check your information and try again."
                    authenticationState.value = INVALID_AUTHENTICATION
                    authenticationState.postValue(ATTEMPTING_AUTHENTICATION)
                }
                null -> {
                }
            }
        }
    }

    fun login(email: String, password: String) {
        if (emailIsValid(email) && passwordIsValid(password))
            userRepository.validateUser(email, password)
    }

    fun createUserAndLogin(email: String, password: String, firstName: String, lastName: String) {
        if (emailIsValid(email) && passwordIsValid(password))
            userRepository.createUser(email, password, firstName, lastName)
    }

    fun getAuthenticationFailedReason(): LiveData<String> {
        return reason
    }

    fun userCancelledRegistration(): Boolean {
        authenticationState.value = UNAUTHENTICATED
        return true
    }

    private fun emailIsValid(email: String): Boolean {
        return if (email.isBlank()) {
            reason.value = "Please enter your email address."
            authenticationState.value = INVALID_AUTHENTICATION
            false
        } else true
    }

    private fun passwordIsValid(password: String): Boolean {
        return if (password.isBlank()) {
            reason.value = "Please enter your password."
            authenticationState.value = INVALID_AUTHENTICATION
            false
        } else true
    }


    /*
//        else
//            false
//    }

//    private fun addNewUser(newUser: User) {
//        user =
//            userDao.getUserById(
//                userDao.addUser(
//                    newUser ) )
//    }

//    fun createAccountAndLogin(firstName: String, lastName: String, email: String, password: String) {
//        addNewUser(
//            User(
//                email = email,
//                password = hashPassword(password),
//                firstName = firstName,
//                lastName = lastName ) )
//        authenticationState.value = AUTHENTICATED
//    }


//    fun authenticateFingerprint(fingerprint: Boolean): Boolean {
//        return fingerprint
////        TODO()
//    }

//    fun logout() {
//        // TODO: clear user
//        authenticationState.value = UNAUTHENTICATED
//    }

//    fun numberOfSavedUsers(): LiveData<Int> {
//        return numberOfUsersSavedOnDevice
//    }

//    fun user(): LiveData<User> { return user }

*/
}

/*
    class CurrentUser {
    val authenticated = MutableLiveData<Boolean>(false)
    val reason = MutableLiveData<String>("")
    val id = MutableLiveData<String>("")
    val firstName = MutableLiveData<String>("")
    val lastName = MutableLiveData<String>("")
    val email = MutableLiveData<String>("")


    fun isAuthenticated(): LiveData<Boolean> { return authenticated }
    fun getAuthenticationResult(): LiveData<String> { return reason }
    fun getID(): LiveData<String> { return id }
    fun getFirstName(): LiveData<String> { return firstName }
    fun getLastName(): LiveData<String> { return  lastName }
    fun getEmail(): LiveData<String> { return email }
}


 */
