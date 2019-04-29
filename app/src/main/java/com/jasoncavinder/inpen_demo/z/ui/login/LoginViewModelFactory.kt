//package com.jasoncavinder.inpen_demo.z.ui.ui
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//import com.jasoncavinder.inpen.demo.data.entities.user.UserRepository
//
///**
// * ViewModel provider factory to instantiate LoginViewModel.
// * Required given LoginViewModel has a non-empty constructor
// */
//class LoginViewModelFactory(
//    private val _userRepository: UserRepository
//) : ViewModelProvider.NewInstanceFactory() {
//
//    @Suppress("UNCHECKED_CAST")
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        return LoginViewModel(_userRepository) as T
//    }
//}
