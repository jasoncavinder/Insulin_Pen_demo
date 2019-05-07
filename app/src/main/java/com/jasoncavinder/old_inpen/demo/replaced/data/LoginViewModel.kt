/*
 * Copyright (c) 2019, Jason Cavinder <jason.cavinder@gmail.com>.
 * This project is licenced to the client of Upwork contract #21949291. It is not
 * licensed for public use. See the LICENSE.md file for details
 */

//package com.jasoncavinder.inpen.demo.data
//
//import android.app.Application
//import android.util.Patterns
//import androidx.lifecycle.AndroidViewModel
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.viewModelScope
//import com.jasoncavinder.inpen.demo.R
//import com.jasoncavinder.insulinpendemoapp.database.entities.pen.Pen
//import com.jasoncavinder.insulinpendemoapp.database.entities.user.User
//import com.jasoncavinder.insulinpendemoapp.database.entities.user.UserProfile
//import com.jasoncavinder.inpen.demo.replaced.login.LoginFormState
//import com.jasoncavinder.inpen.demo.onboarding.CreateUserFormState
//import com.jasoncavinder.inpen.demo.onboarding.CreatedUser
//import com.jasoncavinder.inpen.demo.replaced.data.UserRepository
//import com.jasoncavinder.insulinpendemoapp.utilities.HashUtils
//import com.jasoncavinder.insulinpendemoapp.database.AppDatabase
//import kotlinx.coroutines.launch
//
//class LoginViewModel(application: Application) : AndroidViewModel(application) {
//
////    private var _parentJob = Job()
////    // By default all the coroutines launched in this scope should be using the Main dispatcher
////    private val _coroutineContext: CoroutineContext
////        get() = _parentJob + Dispatchers.Main
//
////    private val scope = CoroutineScope(_coroutineContext)
//
//    private val _loginForm = MutableLiveData<LoginFormState>()
//    val loginFormState: LiveData<LoginFormState> = _loginForm
//
//    private val _createUserForm = MutableLiveData<CreateUserFormState>()
//    val createUserFormState: LiveData<CreateUserFormState> = _createUserForm
//
//    private val _addPenResult = MutableLiveData<Boolean>()
//    val addPenResult: LiveData<Boolean> = _addPenResult
//
//    private val _addProviderResult = MutableLiveData<Boolean>()
//    val addProviderResult: LiveData<Boolean> = _addProviderResult
//
//    private val _newUser = MutableLiveData<CreatedUser>()
//    val newUser: LiveData<CreatedUser> = _newUser
//
//
//    private val _loginResult = MutableLiveData<Boolean>()
//    val loginResult: LiveData<Boolean> = _loginResult
//
//    private val _createUserResult = MutableLiveData<Boolean>()
//    val createUserResult: LiveData<Boolean> = _createUserResult
//
//    private var _user = MutableLiveData<UserProfile>()
//    val user: LiveData<UserProfile> = _user
//
//    private val _userRepository: UserRepository
//    //    private val _loginRepository: LoginRepository
//    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
//    // - We can put an observer on the data (instead of polling for changes) and only update the
//    //   the UI when the data actually changes.
//    // - AppRepository is completely separated from the UI through the ViewModel.
//    val localUsers: LiveData<Int>
//
//
//    init {
//        val userDao = AppDatabase.getInstance(application).userDao()
//        val providerDao = AppDatabase.getInstance(application).providerDao()
//        val penDao = AppDatabase.getInstance(application).penDao()
//        val penDataPointDao = AppDatabase.getInstance(application).penDataPointDao()
//        val doseDao = AppDatabase.getInstance(application).doseDao()
//        val userProfileDao = AppDatabase.getInstance(application).userProfileDao()
//        _userRepository = UserRepository.getInstance(
//            userDao, providerDao, penDao, penDataPointDao, doseDao, userProfileDao
//        )
//
//        localUsers = _userRepository.getLocalUserCount()
//        _userRepository.recoverLoggedInId()?.run {
//            viewModelScope.launch {
//                _userRepository.loadUserProfile(this@run)?.let {
//                    _user = it as MutableLiveData<UserProfile>
//                }
//            }
//        }
//    }
//
//    fun login(email: String, password: String) {
//        viewModelScope.launch {
//            when (when (_userRepository.verifyUser(email, HashUtils.sha512(password))) {
//                null -> _loginResult.postValue(false)
//                else -> _userRepository.loadUserProfile()?.let { _user = it as MutableLiveData<UserProfile> }
//            }) {
//                null -> _loginResult.postValue(false)
//                else -> _loginResult.postValue(true)
//            }
//
//        }
//    }
//
//    fun logout() {
//        viewModelScope.launch {
//            _userRepository.logout()
//        }
//    }
//
//    fun createUser(firstName: String, lastName: String, email: String, password: String) {
//        val user =
//            User(email = email, firstName = firstName, lastName = lastName, password = HashUtils.sha512(password))
//        viewModelScope.launch {
//            _userRepository.createUser(user)
//                .run {
//                    when (this) {
//                        null -> _createUserResult.postValue(false)
//                        else -> {
//                            _createUserResult.postValue(true)
//                            _user = _userRepository.loadUserProfile() as MutableLiveData<UserProfile>
//                        }
//                    }
//                }
//        }
//    }
//
//    fun loginDataChanged(email: String, password: String) {
//        if (!isEmailValid(email)) {
//            _loginForm.value = LoginFormState(emailError = R.string.invalid_email)
//        } else if (!isPasswordValid(password)) {
//            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
//        } else {
//            _loginForm.value = LoginFormState(isDataValid = true)
//        }
//    }
//
//    fun createUserDataChanged(firstName: String, lastName: String, email: String, password: String, confirm: String) {
//        when {
//            !isNameValid(firstName) ->
//                _createUserForm.value =
//                    CreateUserFormState(firstNameError = R.string.invalid_first_name)
//            !isNameValid(lastName) ->
//                _createUserForm.value =
//                    CreateUserFormState(lastNameError = R.string.invalid_last_name)
//            !isEmailValid(email) ->
//                _createUserForm.value =
//                    CreateUserFormState(emailError = R.string.invalid_email)
//            !isPasswordValid(password) ->
//                _createUserForm.value =
//                    CreateUserFormState(passwordError = R.string.invalid_password)
//            !isConfirmValid(password, confirm) ->
//                _createUserForm.value =
//                    CreateUserFormState(confirmError = R.string.invalid_confirm)
//            else ->
//                _createUserForm.value = CreateUserFormState(isDataValid = true)
//        }
//    }
//
//    private fun isNameValid(name: String): Boolean {
//        return name.isNotEmpty()
//    }
//
//    private fun isEmailValid(username: String): Boolean {
//        return Patterns.EMAIL_ADDRESS.matcher(username).matches()
//    }
//
//    private fun isPasswordValid(password: String): Boolean {
//        return password.length >= 8
//    }
//
//    private fun isConfirmValid(password: String, confirm: String): Boolean {
//        return password == confirm
//    }
//
//
//    fun addPen(pen: Pen) {
//        viewModelScope.launch {
//            _userRepository.addPen(pen)
//
////            when (val updatedUser = _newUser.value) {
////                null -> _addPenResult.postValue(false)
////                else -> {
////                    val result = _userRepository.addPen(penPoints)
////                    if (result) _newUser.postValue(
////                        CreatedUser(
////                            userID = updatedUser.userID,
////                            email = updatedUser.email,
////                            firstName = updatedUser.firstName,
////                            lastName = updatedUser.lastName,
////                            penID = penPoints.penID,
////                            providerID = updatedUser.providerID
////                        )
////                    )
////                    _addPenResult.postValue(result)
////                    this@LoginViewModel.userID = updatedUser.userID
////                }
////            }
////        }
//        }
//    }
//
//    fun addProvider(userId: String, provider: String) {
//        TODO()
//    }
//
////    override fun onCleared() {
////        super.onCleared()
////        _parentJob.cancel()
////    }
//
//}
//
