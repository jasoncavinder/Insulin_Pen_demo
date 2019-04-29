//package com.jasoncavinder.inpen_demo.z.viewmodels.user
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//import com.jasoncavinder.inpen.demo.data.entities.user.UserRepository
//
//class UserViewModelFactory (
//    private val _userRepository: UserRepository,
//    private val _userID: String
//) : ViewModelProvider.NewInstanceFactory() {
//
//    @Suppress("UNCHECKED_CAST")
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        return UserViewModel(_userRepository, _userID) as T
//    }
//}
