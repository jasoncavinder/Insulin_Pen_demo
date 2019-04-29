//package com.jasoncavinder.inpen_demo.z.viewmodels.user
//
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.jasoncavinder.inpen.demo.data.entities.user.User
//import com.jasoncavinder.inpen.demo.data.entities.user.UserRepository
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.cancel
//
//class UserViewModel(
//    userRepository: UserRepository,
//    private val _userID: String
//) : ViewModel() {
//
//    lateinit var isLoggedIn: LiveData<Boolean>
//        private set
//
//    lateinit var user: LiveData<List<User>>
//        private set
//
//    @ExperimentalCoroutinesApi
//    override fun onCleared() {
//        super.onCleared()
//        viewModelScope.cancel()
//    }
//
//    init {
//        // TODO:
//    }
//}